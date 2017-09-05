package gov.ehawaii.hacc.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import gov.ehawaii.hacc.dao.GrantsDao;
import gov.ehawaii.hacc.model.Grant;
import gov.ehawaii.hacc.specifications.ColumnSpecification;
import gov.ehawaii.hacc.specifications.DrilldownLocationSpecification;
import gov.ehawaii.hacc.specifications.FilteredGrantsSpecification;
import gov.ehawaii.hacc.specifications.IdSpecification;
import gov.ehawaii.hacc.specifications.OrganizationSpecification;
import gov.ehawaii.hacc.specifications.TopNSpecification;

@Repository("GrantsDao")
public class GrantsDaoImpl extends JdbcDaoSupport implements GrantsDao {

  private static final Logger LOGGER = LogManager.getLogger(GrantsDaoImpl.class);

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
  public List<Grant> findGrants(FilteredGrantsSpecification specification) {
    String countStmt = String.format(SqlStatements.COUNT, specification.getTable()) + specification.toSqlClause();
    Long count = getJdbcTemplate().queryForObject(countStmt, Long.class, specification.getArguments());
    if (count == 0) {
      return new ArrayList<>();
    }
    List<Grant> grants;
    String select = SqlStatements.GET_ALL_GRANTS + specification.toSqlClause();
    if (count > 1) {
      grants = getJdbcTemplate().query(select, rowMapper, specification.getArguments());
    }
    else {
      grants = new ArrayList<>();
      grants.add(getJdbcTemplate().queryForObject(select, rowMapper, specification.getArguments()));
    }
    LOGGER.info("Found " + grants.size() + " grant(s).");
    return grants;
  }


  @Override
  public List<Map<String, Object>> findTopN(final TopNSpecification specification) {
    String stmt = String.format(SqlStatements.GET_TOP_N_DATA, specification.getColumn1(), specification.getColumn2());
    stmt = String.format("%s %s", stmt, specification.toSqlClause());
    LOGGER.info("SQL statement: " + stmt);

    return getJdbcTemplate().query(stmt, rs -> {
      String table, column;
      switch (specification.getColumn1()) {
      case SqlStatements.ORGANIZATION_ID:
        table = Tables.ORGANIZATIONS;
        column = SqlStatements.ORGANIZATION;
        break;
      case SqlStatements.PROJECT_ID:
        table = Tables.PROJECTS;
        column = SqlStatements.PROJECT;
        break;
      default:
        throw new IllegalArgumentException("Unsupported column: " + specification.getColumn1());
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
  public String findValueForId(IdSpecification specification) {
    return getValue(specification.getTable(), specification.getColumn(), specification.getValue());
  }


  @Override
  public List<Map<String, Long>> findDataOverTime(OrganizationSpecification specification) {
    long orgId = findIdForValue(
        new IdSpecification(Tables.ORGANIZATIONS, SqlStatements.ORGANIZATION, specification.getOrganization()));
    String stmt = String.format(SqlStatements.GET_DATA_FOR_ORG_FOR_EACH_FISCAL_YEAR, specification.getColumn(), orgId);
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
  public Map<String, Map<String, Long>> findLocationDataForDrilldown(DrilldownLocationSpecification specification) {
    Map<String, Map<String, Long>> data = new HashMap<>();

    ResultSetExtractor<Map<String, Long>> rsExtractor = rs -> {
      Map<String, Long> map = new HashMap<>();
      while (rs.next()) {
        map.put(rs.getString(1), rs.getLong(2));
      }
      return map;
    };

    String stmt = String.format(SqlStatements.GET_TOTAL_FOR_EACH_LOCATION, specification.getAggregateField());
    data.put("totals", getJdbcTemplate().query(stmt, rsExtractor, specification.getFilterField()));

    List<String> locations = findAllValues(new ColumnSpecification(Tables.LOCATIONS, SqlStatements.LOCATION));

    stmt = String.format(SqlStatements.GET_ALL_DATA_FOR_LOCATION, specification.getAggregateField());
    for (String location : locations) {
      data.put(location, getJdbcTemplate().query(stmt, rsExtractor, location, specification.getFilterField()));
    }

    return data;
  }


  @Override
  public long findIdForValue(IdSpecification specification) {
    Long count = getCount(specification.getTable(), specification.getColumn(), specification.getValue());
    if (count == 0) {
      return -1;
    }
    String stmt = String.format(SqlStatements.GET_ID, specification.getTable(), specification.getColumn(),
        specification.getValue());
    Long id = getJdbcTemplate().queryForObject(stmt, Long.class);
    return id == null ? -1 : id;
  }


  @Override
  public List<String> findAllValues(ColumnSpecification specification) {
    return getJdbcTemplate().queryForList(specification.toSqlClause(), String.class);
  }


  private long saveValue(String tableName, String columnName, String value) {
    IdSpecification specification = new IdSpecification(tableName, columnName, value);
    long id = findIdForValue(specification);
    if (id != -1) {
      return id;
    }
    String stmt = String.format(SqlStatements.INSERT_INTO, tableName, columnName);
    if (getJdbcTemplate().update(stmt, value) > 0) {
      return findIdForValue(specification);
    }
    return -1;
  }


  private String getValue(String tableName, String columnName, String id) {
    long count = getCount(tableName, "ID", id);
    if (count == 0) {
      return "";
    }
    String stmt = String.format(SqlStatements.GET_VALUE, columnName, tableName);
    String data = getJdbcTemplate().queryForObject(stmt, String.class, id);
    return data == null ? "" : data;
  }


  private long getCount(String tableName, String columnName, String value) {
    String stmt = String.format(SqlStatements.COUNT, tableName) + "WHERE " + columnName + " = ?";
    return getJdbcTemplate().queryForObject(stmt, Long.class, value);
  }

}
