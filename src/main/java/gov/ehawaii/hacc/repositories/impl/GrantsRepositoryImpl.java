package gov.ehawaii.hacc.repositories.impl;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.lang.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import com.mysql.jdbc.Statement;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import gov.ehawaii.hacc.model.Grant;
import gov.ehawaii.hacc.repositories.GrantsRepository;
import gov.ehawaii.hacc.repositories.impl.TimeSeries.TimeSeriesDataPoint;
import gov.ehawaii.hacc.specifications.ColumnSpecification;
import gov.ehawaii.hacc.specifications.FilteredSpecification;
import gov.ehawaii.hacc.specifications.IdSpecification;
import gov.ehawaii.hacc.specifications.Specification;
import gov.ehawaii.hacc.specifications.SqlSpecification;
import gov.ehawaii.hacc.specifications.TimeSeriesSpecification;
import gov.ehawaii.hacc.specifications.TopNSpecification;

/**
 * This repository interacts with an SQL database to store or retrieve information about grants.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
@Repository("GrantsRepository")
@SuppressFBWarnings("OBL_UNSATISFIED_OBLIGATION_EXCEPTION_EDGE")
public class GrantsRepositoryImpl extends JdbcDaoSupport implements GrantsRepository {

  private static final Logger LOGGER = LogManager.getLogger(GrantsRepositoryImpl.class);

  private final RowMapper<Grant> rowMapper = (rs, rowNum) -> {
    Grant grant = new Grant();
    grant.setId(rs.getLong(1));
    grant.setGrantStatus(getValue(Tables.GRANT_STATUSES, Columns.GRANT_STATUS, rs.getString(2)));
    grant.setFiscalYear(rs.getInt(3));
    grant.setGrantType(getValue(Tables.GRANT_TYPES, Columns.GRANT_TYPE, rs.getString(4)));
    grant.setOrganization(getValue(Tables.ORGANIZATIONS, Columns.ORGANIZATION, rs.getString(5)));
    grant.setProject(rs.getString(6));
    grant.setAmount(rs.getLong(7));
    grant.setLocation(getValue(Tables.LOCATIONS, Columns.LOCATION, rs.getString(8)));
    grant.setStrategicPriority(
        getValue(Tables.STRATEGIC_PRIORITIES, Columns.STRATEGIC_PRIORITY, rs.getString(9)));
    grant.setStrategicResults(
        getValue(Tables.STRATEGIC_RESULTS, Columns.STRATEGIC_RESULT, rs.getString(10)));
    grant.setTotalNumberServed(rs.getInt(11));
    grant.setNumberNativeHawaiiansServed(rs.getInt(12));
    return grant;
  };

  @Resource(name = "dataSource")
  private DataSource dataSource;

  /**
   * Initializes the data source after an instance of this class is created.
   */
  @PostConstruct
  final void init() {
    setDataSource(dataSource);
    LOGGER.info(GrantsRepositoryImpl.class + " initialized.");
  }

  @Override
  public final boolean insertGrant(final Grant grant) {
    long grantStatusId =
        saveValue(Tables.GRANT_STATUSES, Columns.GRANT_STATUS, grant.getGrantStatus());
    long grantTypeId = saveValue(Tables.GRANT_TYPES, Columns.GRANT_TYPE, grant.getGrantType());
    long locationId = saveValue(Tables.LOCATIONS, Columns.LOCATION, grant.getLocation());
    long organizationId =
        saveValue(Tables.ORGANIZATIONS, Columns.ORGANIZATION, grant.getOrganization());
    long strategicPriorityId = saveValue(Tables.STRATEGIC_PRIORITIES, Columns.STRATEGIC_PRIORITY,
        grant.getStrategicPriority());
    long strategicResultId =
        saveValue(Tables.STRATEGIC_RESULTS, Columns.STRATEGIC_RESULT, grant.getStrategicResults());

    long rows = getJdbcTemplate().update(SqlStatements.INSERT_GRANT, grantStatusId,
        grant.getFiscalYear(), grantTypeId, organizationId, grant.getProject(), grant.getAmount(),
        locationId, strategicPriorityId, strategicResultId, grant.getTotalNumberServed(),
        grant.getNumberNativeHawaiiansServed());
    if (rows > 0) {
      LOGGER.info("Successfully saved grant [" + grant + "] to database.");
      return true;
    }
    else {
      LOGGER.error("Unable to save grant [" + grant + "] to database.");
      return false;
    }
  }

  @Override
  public final boolean insertOrganization(final String organization) {
    return saveValue(Tables.ORGANIZATIONS, Columns.ORGANIZATION, organization) > 0;
  }

  @Override
  public final List<Grant> findGrants(final Specification specification) {
    FilteredSpecification filteredSpecification = (FilteredSpecification) specification;

    String selectStmt =
        String.format(SqlStatements.GET_ALL_GRANTS, filteredSpecification.getTable());
    selectStmt = selectStmt + filteredSpecification.toSqlClause();
    List<Grant> grants =
        getJdbcTemplate().query(selectStmt, rowMapper, filteredSpecification.getFilterValues());

    LOGGER.info("Found " + grants.size() + " grant(s).");
    return grants;
  }

  @Override
  public final List<Map<String, Object>> findTopN(final Specification specification) {
    TopNSpecification topNSpecification = (TopNSpecification) specification;

    String groupBy = topNSpecification.getGroupByColumn();

    return getJdbcTemplate().query(topNSpecification.toSqlClause(), rs -> {
      String table, column;
      switch (groupBy) {
      case Columns.ORGANIZATION_ID:
        table = Tables.ORGANIZATIONS;
        column = Columns.ORGANIZATION;
        break;
      default:
        throw new IllegalArgumentException("Unsupported column: " + groupBy);
      }

      List<Map<String, Object>> rows = new ArrayList<>();
      while (rs.next()) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("key", getValue(table, column, rs.getString(1)));
        row.put("value", rs.getLong(2));
        rows.add(row);
      }
      return rows;
    });
  }

  @Override
  public final String findValueForId(final Specification specification) {
    SqlSpecification sqlSpecification = (SqlSpecification) specification;
    return getValue(sqlSpecification.getTable(), sqlSpecification.getColumn(),
        sqlSpecification.getValue());
  }

  @Override
  public final List<TimeSeries> findTimeSeriesData(final Specification specification) {
    TimeSeriesSpecification tsSpecification = (TimeSeriesSpecification) specification;

    List<String> ids = getJdbcTemplate().query(tsSpecification.getAggregateQuery(), rs -> {
      List<String> list = new ArrayList<>();
      while (rs.next()) {
        list.add(rs.getString(1));
      }
      return list;
    }, ArrayUtils.addAll(tsSpecification.getFilterValues(), new Object[] { tsSpecification.getTop() }));

    List<TimeSeries> seriesList = new ArrayList<>();

    for (String id : ids) {
      String value = getValue(tsSpecification.getTable(), tsSpecification.getColumn(), id);

      TimeSeries series = new TimeSeries();
      series.setSeriesName(value);
      series.setPoints(getJdbcTemplate().query(tsSpecification.getTimeSeriesQuery(), rs -> {
        List<TimeSeriesDataPoint> points = new ArrayList<>();
        while (rs.next()) {
          points.add(new TimeSeriesDataPoint(rs.getInt(1), rs.getLong(2)));
        }
        return points;
      }, ArrayUtils.addAll(tsSpecification.getFilterValues(), new Object[] { id })));
      seriesList.add(series);
    }

    int minimum = Integer.MAX_VALUE;
    int maximum = Integer.MIN_VALUE;
    for (TimeSeries ts : seriesList) {
      for (TimeSeriesDataPoint point : ts.getPoints()) {
        int year = point.getYear();
        if (year < minimum) {
          minimum = year;
        }
        if (year > maximum) {
          maximum = year;
        }
      }
    }

    for (TimeSeries series : seriesList) {
      series.setStartYear(minimum);
      series.setEndYear(maximum);
      series.fillInMissingData();
    }

    return seriesList;
  }

  @Override
  public final Map<String, Map<String, Long>> findAggregateData(final Specification specification) {
    SqlSpecification totalsSpecification = (SqlSpecification) specification;

    Map<String, Map<String, Long>> data = new HashMap<>();

    ResultSetExtractor<Map<String, Long>> rsExtractor = rs -> {
      Map<String, Long> map = new LinkedHashMap<>();
      while (rs.next()) {
        map.put(rs.getString(1), rs.getLong(2));
      }
      return map;
    };

    String sqlClause = totalsSpecification.toSqlClause();
    Object[] filterValues = totalsSpecification.getFilterValues();

    ColumnSpecification colSpec = totalsSpecification.getColSpec();
    if (colSpec == null) {
      data.put("totals", getJdbcTemplate().query(sqlClause, rsExtractor, filterValues));
      return data;
    }

    List<String> values = findAllValues(colSpec);

    for (String value : values) {
      IdSpecification idSpec = new IdSpecification(colSpec.getTable(), colSpec.getColumn(), value);
      Object[] id = new Object[] { findIdForValue(idSpec) };
      filterValues = ArrayUtils.addAll(id, totalsSpecification.getFilterValues());
      data.put(value, getJdbcTemplate().query(sqlClause, rsExtractor, filterValues));
    }

    return data;
  }

  @Override
  public final long findIdForValue(final Specification specification) {
    SqlSpecification sqlSpecification = (SqlSpecification) specification;

    String table = sqlSpecification.getTable();
    String column = sqlSpecification.getColumn();
    Object value = sqlSpecification.getValue();

    String stmt = String.format(SqlStatements.GET_ID, table, column, value);
    try {
      return getJdbcTemplate().queryForObject(stmt, Long.class);
    }
    catch (DataAccessException dae) {
      LOGGER.error("An error occurred while trying to execute the following query: " + stmt, dae);
      return -1;
    }
  }

  @Override
  public final List<String> findAllValues(final Specification specification) {
    SqlSpecification sqlSpecification = (SqlSpecification) specification;
    return getJdbcTemplate().queryForList(sqlSpecification.toSqlClause(), String.class);
  }

  /**
   * Saves the given value in the database.
   * 
   * @param tableName The name of the table in which to save the given data.
   * @param columnName The name of the column that will contain the given data.
   * @param value The data to save.
   * @return The ID of the row that contains the data that was saved.
   */
  private long saveValue(final String tableName, final String columnName, final Object value) {
    long id = findIdForValue(new IdSpecification(tableName, columnName, value));
    if (id != -1) {
      return id;
    }
    GeneratedKeyHolder holder = new GeneratedKeyHolder();
    getJdbcTemplate().update(conn -> {
      String stmt = String.format(SqlStatements.INSERT_INTO, tableName, columnName);
      PreparedStatement ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, value.toString());
      return ps;
    }, holder);
    return holder.getKey().longValue();
  }

  /**
   * Retrieves a value from the database.
   * 
   * @param tableName The name of the table from which to retrieve a value.
   * @param columnName The name of the column that contains the value.
   * @param id The row number.
   * @return A string representing the value, or an empty string if there is none.
   */
  private String getValue(final String tableName, final String columnName, final Object id) {
    String stmt = String.format(SqlStatements.GET_VALUE, columnName, tableName);
    try {
      String result = getJdbcTemplate().queryForObject(stmt, String.class, id);
      return result == null ? "" : result;
    }
    catch (DataAccessException dae) {
      LOGGER.error("An error occurred while trying to execute the following query: " + stmt, dae);
      return "";
    }
  }

}
