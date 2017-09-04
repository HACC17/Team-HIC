package gov.ehawaii.hacc.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import gov.ehawaii.hacc.dao.GrantsDao;
import gov.ehawaii.hacc.model.Grant;

@Repository("GrantsDao")
public class GrantsDaoImpl extends JdbcDaoSupport implements GrantsDao {

  private static final Logger LOGGER = LogManager.getLogger(GrantsDaoImpl.class);

  private final RowMapper<Grant> rowMapper = new RowMapper<Grant>() {

    @Override
    public Grant mapRow(ResultSet rs, int rowNum) throws SQLException {
      Grant grant = new Grant();
      grant.setId(rs.getLong(1));
      grant.setGrantStatus(getValue(SqlStatements.GRANT_STATUSES, "STATUS", rs.getLong(2)));
      grant.setFiscalYear(rs.getInt(3));
      grant.setGrantType(getValue(SqlStatements.GRANT_TYPES, "GRANT_TYPE", rs.getLong(4)));
      grant.setOrganization(getValue(SqlStatements.ORGANIZATIONS, "ORGANIZATION", rs.getLong(5)));
      grant.setProject(getValue(SqlStatements.PROJECTS, "PROJECT", rs.getLong(6)));
      grant.setAmount(rs.getInt(7));
      grant.setLocation(getValue(SqlStatements.LOCATIONS, "LOCATION", rs.getLong(8)));
      grant.setStrategicPriority(
          getValue(SqlStatements.STRATEGIC_PRIORITIES, "STRATEGIC_PRIORITY", rs.getLong(9)));
      grant.setStrategicResults(
          getValue(SqlStatements.STRATEGIC_RESULTS, "STRATEGIC_RESULT", rs.getLong(10)));
      grant.setTotalNumberServed(rs.getInt(11));
      grant.setNumberNativeHawaiiansServed(rs.getInt(12));
      return grant;
    }

  };

  @Resource(name = "dataSource")
  private DataSource dataSource;

  @PostConstruct
  void init() {
    setDataSource(dataSource);
    LOGGER.info("GrantDaoImpl initialized.");
  }

  @Override
  public boolean saveGrant(Grant grant) {
    long grantStatusId = getId(SqlStatements.GRANT_STATUSES, "STATUS", grant.getGrantStatus());
    if (grantStatusId == -1) {
      grantStatusId = saveValue(SqlStatements.GRANT_STATUSES, "STATUS", grant.getGrantStatus());
    }

    long grantTypeId = getId(SqlStatements.GRANT_TYPES, "GRANT_TYPE", grant.getGrantType());
    if (grantTypeId == -1) {
      grantTypeId = saveValue(SqlStatements.GRANT_TYPES, "GRANT_TYPE", grant.getGrantType());
    }

    long locationId = getId(SqlStatements.LOCATIONS, "LOCATION", grant.getLocation());
    if (locationId == -1) {
      locationId = saveValue(SqlStatements.LOCATIONS, "LOCATION", grant.getLocation());
    }

    long organizationId =
        getId(SqlStatements.ORGANIZATIONS, "ORGANIZATION", grant.getOrganization());
    if (organizationId == -1) {
      organizationId =
          saveValue(SqlStatements.ORGANIZATIONS, "ORGANIZATION", grant.getOrganization());
    }

    long projectId = getId(SqlStatements.PROJECTS, "PROJECT", grant.getProject());
    if (projectId == -1) {
      projectId = saveValue(SqlStatements.PROJECTS, "PROJECT", grant.getProject());
    }

    long strategicPriorityId = getId(SqlStatements.STRATEGIC_PRIORITIES, "STRATEGIC_PRIORITY",
        grant.getStrategicPriority());
    if (strategicPriorityId == -1) {
      strategicPriorityId = saveValue(SqlStatements.STRATEGIC_PRIORITIES, "STRATEGIC_PRIORITY",
          grant.getStrategicPriority());
    }

    long strategicResultId =
        getId(SqlStatements.STRATEGIC_RESULTS, "STRATEGIC_RESULT", grant.getStrategicResults());
    if (strategicResultId == -1) {
      strategicResultId = saveValue(SqlStatements.STRATEGIC_RESULTS, "STRATEGIC_RESULT",
          grant.getStrategicResults());
    }

    long rows = getJdbcTemplate().update(SqlStatements.INSERT_GRANT, grantStatusId,
        grant.getFiscalYear(), grantTypeId, organizationId, projectId, grant.getAmount(),
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
  public List<Grant> getGrants(String filter, Object[] arguments) {
    String whereClause = (arguments.length > 0 ? " WHERE " + filter : "");
    String stmt = "SELECT COUNT(*) FROM GRANTS" + whereClause;
    Long count = getJdbcTemplate().queryForObject(stmt, Long.class, arguments);
    if (count == 0) {
      return new ArrayList<>();
    }
    stmt = SqlStatements.GET_ALL_GRANTS + whereClause;
    List<Grant> grants;
    if (count > 1) {
      grants = getJdbcTemplate().query(stmt, rowMapper, arguments);
    }
    else {
      grants = new ArrayList<>();
      grants.add(getJdbcTemplate().queryForObject(stmt, rowMapper, arguments));
    }
    LOGGER.info("Found " + grants.size() + " grant(s).");
    return grants;
  }

  @Override
  public List<Grant> findTopFiveGrantsForFiscalYear(int fiscalYear) {
    String stmt = String.format(SqlStatements.GET_TOTAL_AMOUNTS_FOR_EACH_ORG, fiscalYear);
    List<Grant> grants = getJdbcTemplate().query(stmt, new ResultSetExtractor<List<Grant>>() {

      @Override
      public List<Grant> extractData(ResultSet rs) throws SQLException {
        List<Grant> grants = new ArrayList<>();
        while (rs.next()) {
          Grant grant = new Grant();
          grant.setOrganization(
              getValue(SqlStatements.ORGANIZATIONS, "ORGANIZATION", rs.getLong(1)));
          grant.setAmount(rs.getLong(2));
          grants.add(grant);
        }
        return grants;
      }

    });
    LOGGER.info("Found top " + grants.size() + " grant(s) for fiscal year " + fiscalYear + ".");
    return grants;
  }

  @Override
  public List<Map<String, Object>> getTopNGrants(int top, final String field1, String field2) {
    String stmt = String.format(SqlStatements.GET_TOP_N_DATA, field1, field2, field1, field2, field2, top);

    List<Map<String, Object>> grants =
        getJdbcTemplate().query(stmt, new ResultSetExtractor<List<Map<String, Object>>>() {

          @Override
          public List<Map<String, Object>> extractData(ResultSet rs) throws SQLException {
            List<Map<String, Object>> rows = new ArrayList<>();
            while (rs.next()) {
              Map<String, Object> row = new LinkedHashMap<>();
              switch (field1) {
              case "ORGANIZATION":
                row.put("key",
                    getValue(SqlStatements.ORGANIZATIONS, "ORGANIZATION", rs.getLong(1)));
                break;
              case "PROJECT":
                row.put("key", getValue(SqlStatements.PROJECTS, "PROJECT", rs.getLong(1)));
                break;
              default:
                throw new IllegalArgumentException(field1 + " not supported, yet.");
              }
              row.put("value", rs.getLong(2));
              rows.add(row);
            }
            return rows;
          }

        });
    LOGGER.info("Found the top " + grants.size() + " " + field1.toLowerCase() + "(s) by "
        + field2.toLowerCase() + ".");
    return grants;
  }

  @Override
  public String getGrantStatusForId(int grantStatusId) {
    return getValue(SqlStatements.GRANT_STATUSES, "STATUS", grantStatusId);
  }

  @Override
  public List<Map<String, Long>> getOrganizationDataOverTime(String organization, String field) {
    long orgId = getId(SqlStatements.ORGANIZATIONS, "ORGANIZATION", organization);
    String stmt = String.format(SqlStatements.GET_DATA_FOR_ORG_FOR_EACH_FISCAL_YEAR, field, orgId);
    return getJdbcTemplate().query(stmt, new ResultSetExtractor<List<Map<String, Long>>>() {

      @Override
      public List<Map<String, Long>> extractData(ResultSet rs)
          throws SQLException, DataAccessException {
        List<Map<String, Long>> rows = new ArrayList<>();
        while (rs.next()) {
          Map<String, Long> row = new LinkedHashMap<>();
          row.put("value", rs.getLong(1));
          row.put("year", rs.getLong(2));
          rows.add(row);
        }
        return rows;
      }

    });
  }

  @Override
  public long getId(String tableName, String columnName, String value) {
    Long count = getCount(tableName, columnName, value);
    if (count == 0) {
      return -1;
    }
    String stmt = String.format(SqlStatements.GET_ID, tableName, columnName);
    Long id = getJdbcTemplate().queryForObject(stmt, Long.class, value);
    LOGGER.info("Retrieved ID for value [" + value + "] from table [" + tableName + "]: " + id);
    return id == null ? -1 : id;
  }

  @Override
  public List<String> getAllGrantStatuses() {
    List<String> statuses =
        getJdbcTemplate().queryForList(SqlStatements.GET_ALL_STATUSES, String.class);
    LOGGER.info("Found " + statuses.size() + " grant statuses.");
    return statuses;
  }

  @Override
  public List<String> getAllGrantTypes() {
    List<String> types =
        getJdbcTemplate().queryForList(SqlStatements.GET_ALL_GRANT_TYPES, String.class);
    LOGGER.info("Found " + types.size() + " grant type(s).");
    return types;
  }

  @Override
  public List<String> getAllLocations() {
    List<String> locations =
        getJdbcTemplate().queryForList(SqlStatements.GET_ALL_LOCATIONS, String.class);
    LOGGER.info("Found " + locations.size() + " location(s).");
    return locations;
  }

  @Override
  public List<String> getAllOrganizations() {
    List<String> organizations =
        getJdbcTemplate().queryForList(SqlStatements.GET_ALL_ORGANIZATIONS, String.class);
    LOGGER.info("Found " + organizations.size() + " organizations.");
    return organizations;
  }

  @Override
  public List<String> getAllStrategicPriorities() {
    List<String> priorities =
        getJdbcTemplate().queryForList(SqlStatements.GET_ALL_STRATEGIC_PRIORITIES, String.class);
    LOGGER.info("Found " + priorities.size() + " strategic priorit(ies).");
    return priorities;
  }

  @Override
  public List<String> getAllStrategicResults() {
    List<String> results =
        getJdbcTemplate().queryForList(SqlStatements.GET_ALL_STRATEGIC_RESULTS, String.class);
    LOGGER.info("Found " + results.size() + " strategic result(s).");
    return results;
  }

  private long saveValue(String tableName, String columnName, String value) {
    String stmt = String.format(SqlStatements.INSERT_INTO, tableName, columnName);
    if (getJdbcTemplate().update(stmt, value) > 0) {
      LOGGER.info("Successfully saved value [" + value + "] into table [" + tableName + "].");
      return getId(tableName, columnName, value);
    }
    return -1;
  }

  private String getValue(String tableName, String columnName, long id) {
    Long count = getCount(tableName, columnName, "ID");
    if (count == 0) {
      return "";
    }
    String stmt = String.format(SqlStatements.GET_VALUE, columnName, tableName);
    String data = getJdbcTemplate().queryForObject(stmt, String.class, id);
    LOGGER.info("Retrieved data for ID [" + id + "] from table [" + tableName + "]: " + data);
    return data == null ? "" : data;
  }

  private long getCount(String tableName, String columnName, String value) {
    String stmt = String.format(SqlStatements.COUNT, tableName, columnName);
    return getJdbcTemplate().queryForObject(stmt, Long.class, value);
  }

}
