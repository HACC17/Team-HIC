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
import org.springframework.util.StringUtils;
import com.mysql.jdbc.Statement;
import gov.ehawaii.hacc.model.Grant;
import gov.ehawaii.hacc.repositories.GrantsRepository;
import gov.ehawaii.hacc.specifications.AggregateSpecification;
import gov.ehawaii.hacc.specifications.FilteredSpecification;
import gov.ehawaii.hacc.specifications.IdSpecification;
import gov.ehawaii.hacc.specifications.Specification;
import gov.ehawaii.hacc.specifications.SqlSpecification;
import gov.ehawaii.hacc.specifications.TimeSeriesSpecification;
import gov.ehawaii.hacc.specifications.TopNSpecification;

@Repository("GrantsRepository")
public class GrantsRepositoryImpl extends JdbcDaoSupport implements GrantsRepository {

  private static final Logger LOGGER = LogManager.getLogger(GrantsRepositoryImpl.class);

  private final RowMapper<Grant> rowMapper = (rs, rowNum) -> {
    Grant grant = new Grant();
    grant.setId(rs.getLong(1));
    grant.setGrantStatus(getValue(Tables.GRANT_STATUSES, SqlStatements.GRANT_STATUS, rs.getString(2)));
    grant.setFiscalYear(rs.getInt(3));
    grant.setGrantType(getValue(Tables.GRANT_TYPES, SqlStatements.GRANT_TYPE, rs.getString(4)));
    grant.setOrganization(getValue(Tables.ORGANIZATIONS, SqlStatements.ORGANIZATION, rs.getString(5)));
    grant.setProject(getValue(Tables.PROJECTS, SqlStatements.PROJECT, rs.getString(6)));
    grant.setAmount(rs.getInt(7));
    grant.setLocation(getValue(Tables.LOCATIONS, SqlStatements.LOCATION, rs.getString(8)));
    grant.setStrategicPriority(getValue(Tables.STRATEGIC_PRIORITIES, SqlStatements.STRATEGIC_PRIORITY, rs.getString(9)));
    grant.setStrategicResults(getValue(Tables.STRATEGIC_RESULTS, SqlStatements.STRATEGIC_RESULT, rs.getString(10)));
    grant.setTotalNumberServed(rs.getInt(11));
    grant.setNumberNativeHawaiiansServed(rs.getInt(12));
    return grant;
  };

  @Resource(name = "dataSource")
  private DataSource dataSource;


  @PostConstruct
  void init() {
    setDataSource(dataSource);
    LOGGER.info("GrantDaoImpl initialized.");
  }


  @Override
  public boolean insertGrant(Grant grant) {
    long grantStatusId = saveValue(Tables.GRANT_STATUSES, SqlStatements.GRANT_STATUS, grant.getGrantStatus());
    long grantTypeId = saveValue(Tables.GRANT_TYPES, SqlStatements.GRANT_TYPE, grant.getGrantType());
    long locationId = saveValue(Tables.LOCATIONS, SqlStatements.LOCATION, grant.getLocation());
    long organizationId = saveValue(Tables.ORGANIZATIONS, SqlStatements.ORGANIZATION, grant.getOrganization());
    long projectId = saveValue(Tables.PROJECTS, SqlStatements.PROJECT, grant.getProject());
    long strategicPriorityId =
        saveValue(Tables.STRATEGIC_PRIORITIES, SqlStatements.STRATEGIC_PRIORITY, grant.getStrategicPriority());
    long strategicResultId =
        saveValue(Tables.STRATEGIC_RESULTS, SqlStatements.STRATEGIC_RESULT, grant.getStrategicResults());

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

    Object[] arguments = filteredSpecification.getArguments();

    String countStmt = String.format(SqlStatements.COUNT, filteredSpecification.getTable());
    countStmt = countStmt + filteredSpecification.toSqlClause();
    Long count = getJdbcTemplate().queryForObject(countStmt, Long.class, arguments);
    if (count == 0) {
      return new ArrayList<>();
    }

    List<Grant> grants;
    String selectStmt = String.format(SqlStatements.GET_ALL_GRANTS, filteredSpecification.getTable());
    selectStmt = selectStmt + filteredSpecification.toSqlClause();
    if (count > 1) {
      grants = getJdbcTemplate().query(selectStmt, rowMapper, arguments);
    }
    else {
      grants = new ArrayList<>();
      grants.add(getJdbcTemplate().queryForObject(selectStmt, rowMapper, arguments));
    }

    LOGGER.info("Found " + grants.size() + " grant(s).");
    return grants;
  }


  @Override
  public List<Map<String, Object>> findTopN(Specification specification) {
    TopNSpecification topNSpecification = (TopNSpecification) specification;

    String column1 = topNSpecification.getColumn1();

    return getJdbcTemplate().query(topNSpecification.toSqlClause(), rs -> {
      String table, column;
      switch (column1) {
      case SqlStatements.ORGANIZATION_ID:
        table = Tables.ORGANIZATIONS;
        column = SqlStatements.ORGANIZATION;
        break;
      case SqlStatements.PROJECT_ID:
        table = Tables.PROJECTS;
        column = SqlStatements.PROJECT;
        break;
      default:
        throw new IllegalArgumentException("Unsupported column: " + column1);
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
    String timeSeriesQuery = tsSpecification.toSqlClause();
    String stmt = String.format(timeSeriesQuery, tsSpecification.getAggregateField(), id);
    LOGGER.info("SQL Statement: " + stmt);

    return getJdbcTemplate().query(stmt, rs -> {
      List<Map<String, Long>> rows = new ArrayList<>();
      while (rs.next()) {
        Map<String, Long> row = new LinkedHashMap<>();
        row.put("key", rs.getLong(1));
        row.put("value", rs.getLong(2));
        rows.add(row);
      }
      return rows;
    });
  }


  @Override
  public Map<String, Map<String, Long>> findAggregateData(Specification specification) {
    AggregateSpecification aggregateSpecification = (AggregateSpecification) specification;

    Map<String, Map<String, Long>> data = new HashMap<>();

    ResultSetExtractor<Map<String, Long>> rsExtractor = rs -> {
      Map<String, Long> map = new LinkedHashMap<>();
      while (rs.next()) {
        map.put(rs.getString(1), rs.getLong(2));
      }
      return map;
    };

    Object[] filterValues = aggregateSpecification.getFilterValues();

    aggregateSpecification.setUseAllQuery(false);

    data.put("totals", getJdbcTemplate().query(aggregateSpecification.toSqlClause(), rsExtractor, filterValues));

    if (StringUtils.isEmpty(aggregateSpecification.getAllQuery())) {
      return data;
    }

    List<String> values = findAllValues(aggregateSpecification.getColSpec());

    aggregateSpecification.setUseAllQuery(true);

    for (String value : values) {
      filterValues = ArrayUtils.addAll(new Object[] { value }, aggregateSpecification.getFilterValues());
      data.put(value, getJdbcTemplate().query(aggregateSpecification.toSqlClause(), rsExtractor, filterValues));
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
