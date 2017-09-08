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
import gov.ehawaii.hacc.specifications.FilteredSpecification;
import gov.ehawaii.hacc.specifications.IdSpecification;
import gov.ehawaii.hacc.specifications.Specification;
import gov.ehawaii.hacc.specifications.SqlSpecification;
import gov.ehawaii.hacc.specifications.TimeSeriesSpecification;
import gov.ehawaii.hacc.specifications.TopNSpecification;
import gov.ehawaii.hacc.specifications.TotalsSpecification;

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
    grant.setProject(getValue(Tables.PROJECTS, Columns.PROJECT, rs.getString(6)));
    grant.setAmount(rs.getInt(7));
    grant.setLocation(getValue(Tables.LOCATIONS, Columns.LOCATION, rs.getString(8)));
    grant.setStrategicPriority(getValue(Tables.STRATEGIC_PRIORITIES, Columns.STRATEGIC_PRIORITY, rs.getString(9)));
    grant.setStrategicResults(getValue(Tables.STRATEGIC_RESULTS, Columns.STRATEGIC_RESULT, rs.getString(10)));
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
  void init() {
    setDataSource(dataSource);
    LOGGER.info("GrantDaoImpl initialized.");
  }


  @Override
  public boolean insertGrant(Grant grant) {
    long grantStatusId = saveValue(Tables.GRANT_STATUSES, Columns.GRANT_STATUS, grant.getGrantStatus());
    long grantTypeId = saveValue(Tables.GRANT_TYPES, Columns.GRANT_TYPE, grant.getGrantType());
    long locationId = saveValue(Tables.LOCATIONS, Columns.LOCATION, grant.getLocation());
    long organizationId = saveValue(Tables.ORGANIZATIONS, Columns.ORGANIZATION, grant.getOrganization());
    long projectId = saveValue(Tables.PROJECTS, Columns.PROJECT, grant.getProject());
    long strategicPriorityId =
        saveValue(Tables.STRATEGIC_PRIORITIES, Columns.STRATEGIC_PRIORITY, grant.getStrategicPriority());
    long strategicResultId =
        saveValue(Tables.STRATEGIC_RESULTS, Columns.STRATEGIC_RESULT, grant.getStrategicResults());

    long rows = getJdbcTemplate().update(SqlStatements.INSERT_GRANT, grantStatusId, grant.getFiscalYear(), grantTypeId,
        organizationId, projectId, grant.getAmount(), locationId, strategicPriorityId, strategicResultId,
        grant.getTotalNumberServed(), grant.getNumberNativeHawaiiansServed());
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
  public List<Grant> findGrants(Specification specification) {
    FilteredSpecification filteredSpecification = (FilteredSpecification) specification;

    String selectStmt = String.format(SqlStatements.GET_ALL_GRANTS, filteredSpecification.getTable());
    selectStmt = selectStmt + filteredSpecification.toSqlClause();
    List<Grant> grants = getJdbcTemplate().query(selectStmt, rowMapper, filteredSpecification.getArguments());

    LOGGER.info("Found " + grants.size() + " grant(s).");
    return grants;
  }


  @Override
  public List<Map<String, Object>> findTopN(Specification specification) {
    TopNSpecification topNSpecification = (TopNSpecification) specification;

    String groupBy = topNSpecification.getGroupByColumn();

    return getJdbcTemplate().query(topNSpecification.toSqlClause(), rs -> {
      String table, column;
      switch (groupBy) {
      case Columns.ORGANIZATION_ID:
        table = Tables.ORGANIZATIONS;
        column = Columns.ORGANIZATION;
        break;
      case Columns.PROJECT_ID:
        table = Tables.PROJECTS;
        column = Columns.PROJECT;
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
  public String findValueForId(Specification specification) {
    SqlSpecification sqlSpecification = (SqlSpecification) specification;
    return getValue(sqlSpecification.getTable(), sqlSpecification.getColumn(), sqlSpecification.getValue());
  }


  @Override
  public List<Map<String, Long>> findTimeSeriesData(Specification specification) {
    TimeSeriesSpecification tsSpecification = (TimeSeriesSpecification) specification;

    long id = findIdForValue(new IdSpecification(tsSpecification.getTable(),
        tsSpecification.getColumn(), tsSpecification.getValue()));

    return getJdbcTemplate().query(tsSpecification.toSqlClause(), rs -> {
      List<Map<String, Long>> rows = new ArrayList<>();
      while (rs.next()) {
        Map<String, Long> row = new LinkedHashMap<>();
        row.put("key", rs.getLong(1));
        row.put("value", rs.getLong(2));
        rows.add(row);
      }
      return rows;
    }, id);
  }


  @Override
  public Map<String, Map<String, Long>> findAggregateData(Specification specification) {
    TotalsSpecification totalsSpecification = (TotalsSpecification) specification;

    Map<String, Map<String, Long>> data = new HashMap<>();

    ResultSetExtractor<Map<String, Long>> rsExtractor = rs -> {
      Map<String, Long> map = new LinkedHashMap<>();
      while (rs.next()) {
        map.put(rs.getString(1), rs.getLong(2));
      }
      return map;
    };

    Object[] filterValues = totalsSpecification.getFilterValues();

    if (totalsSpecification.getColSpec() == null) {
      data.put("totals", getJdbcTemplate().query(totalsSpecification.toSqlClause(), rsExtractor, filterValues));
      return data;
    }

    List<String> values = findAllValues(totalsSpecification.getColSpec());

    for (String value : values) {
      filterValues = ArrayUtils.addAll(new Object[] { value }, totalsSpecification.getFilterValues());
      data.put(value, getJdbcTemplate().query(totalsSpecification.toSqlClause(), rsExtractor, filterValues));
    }

    return data;
  }


  @Override
  public long findIdForValue(Specification specification) {
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
  public List<String> findAllValues(Specification specification) {
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
  private long saveValue(String tableName, String columnName, Object value) {
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
  private String getValue(String tableName, String columnName, Object id) {
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
