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
      grant.setLocation(rs.getString(8));
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
    long grantTypeId = getId(SqlStatements.GRANT_TYPES, "GRANT_TYPE", grant.getGrantType());
    if (grantTypeId == -1) {
      grantTypeId = saveValue(SqlStatements.GRANT_TYPES, "GRANT_TYPE", grant.getGrantType());
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

    long grantStatusId = getId(SqlStatements.GRANT_STATUSES, "STATUS", grant.getGrantStatus());
    if (grantStatusId == -1) {
      grantStatusId = saveValue(SqlStatements.GRANT_STATUSES, "STATUS", grant.getGrantStatus());
    }

    long rows = getJdbcTemplate().update(SqlStatements.INSERT_GRANT, grantStatusId,
        grant.getFiscalYear(), grantTypeId, organizationId, projectId, grant.getAmount(),
        grant.getLocation(), strategicPriorityId, strategicResultId, grant.getTotalNumberServed(),
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
  public List<Grant> retrieveAll() {
    Long count = getJdbcTemplate().queryForObject("SELECT COUNT(*) FROM GRANTS", Long.class);
    if (count == 0) {
      return new ArrayList<>();
    }
    List<Grant> grants;
    if (count > 1) {
      grants = getJdbcTemplate().query(SqlStatements.GET_ALL_GRANTS, rowMapper);
    }
    else {
      grants = new ArrayList<>();
      grants.add(getJdbcTemplate().queryForObject(SqlStatements.GET_ALL_GRANTS, rowMapper));
    }
    LOGGER.info("Found " + grants.size() + " grant(s).");
    return grants;
  }

  @Override
  public List<Grant> findGrantsByFiscalYear(int fiscalYear) {
    String stmt = "SELECT ORGANIZATION_ID, SUM(AMOUNT) FROM GRANTS WHERE FISCAL_YEAR = "
        + fiscalYear + " GROUP BY ORGANIZATION_ID, AMOUNT ORDER BY AMOUNT DESC LIMIT 5";
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
  public List<Map<String, Object>> retrieveTop(int top, String field, String criterion) {
    String stmt = "SELECT " + field + "_ID, SUM(" + criterion + ") FROM GRANTS GROUP BY " + field
        + "_ID, " + criterion + " ORDER BY " + criterion + " DESC LIMIT " + top;

    List<Map<String, Object>> grants;
    switch (field) {
    case "ORGANIZATION":
      grants = getJdbcTemplate().query(stmt, new ResultSetExtractor<List<Map<String, Object>>>() {

        @Override
        public List<Map<String, Object>> extractData(ResultSet rs) throws SQLException {
          List<Map<String, Object>> maps = new ArrayList<>();
          while (rs.next()) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("key", getValue(SqlStatements.ORGANIZATIONS, "ORGANIZATION", rs.getLong(1)));
            map.put("value", rs.getLong(2));
            maps.add(map);
          }
          return maps;
        }

      });
      break;
    case "PROJECT":
      grants = getJdbcTemplate().query(stmt, new ResultSetExtractor<List<Map<String, Object>>>() {

        @Override
        public List<Map<String, Object>> extractData(ResultSet rs) throws SQLException {
          List<Map<String, Object>> maps = new ArrayList<>();
          while (rs.next()) {
            Map<String, Object> map = new LinkedHashMap<>();
            String project = getValue(SqlStatements.PROJECTS, "PROJECT", rs.getLong(1));
            if (project.length() > 50) {
              project = project.substring(0, 50).trim() + "...";
            }
            map.put("key", project);
            map.put("value", rs.getLong(2));
            maps.add(map);
          }
          return maps;
        }

      });
      break;
    default:
      throw new IllegalArgumentException(field + " not supported, yet.");
    }
    LOGGER.info("Found the top " + grants.size() + " " + field.toLowerCase() + "(s) by "
        + criterion.toLowerCase() + ".");
    return grants;
  }

  @Override
  public String getGrantStatusForId(int grantStatusId) {
    return getValue(SqlStatements.GRANT_STATUSES, "STATUS", grantStatusId);
  }

  private List<Grant> getGrantsBy(String columnName, Object columnValue) {
    String stmt = String.format(SqlStatements.COUNT, "GRANTS", columnName);
    Long count = getJdbcTemplate().queryForObject(stmt, Long.class, columnValue);
    if (count == 0) {
      return new ArrayList<>();
    }

    stmt = String.format(SqlStatements.GET_GRANT_BY, columnName);
    if (count > 1) {
      return getJdbcTemplate().query(stmt, rowMapper, columnValue);
    }
    else {
      List<Grant> grants = new ArrayList<>();
      grants.add(getJdbcTemplate().queryForObject(stmt, new Object[] { columnValue }, rowMapper));
      return grants;
    }
  }

  private long getId(String tableName, String columnName, String value) {
    String stmt = String.format(SqlStatements.COUNT, tableName, columnName);
    Long count = getJdbcTemplate().queryForObject(stmt, Long.class, value);
    if (count == 0) {
      return -1;
    }
    stmt = String.format(SqlStatements.GET_ID, tableName, columnName);
    Long id = getJdbcTemplate().queryForObject(stmt, Long.class, value);
    LOGGER.info("Retrieved ID for value [" + value + "] from table [" + tableName + "]: " + id);
    return id == null ? -1 : id;
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
    String stmt = String.format(SqlStatements.COUNT, tableName, "ID");
    Long count = getJdbcTemplate().queryForObject(stmt, Long.class, id);
    if (count == 0) {
      return "";
    }
    stmt = String.format(SqlStatements.GET_VALUE, columnName, tableName);
    String data = getJdbcTemplate().queryForObject(stmt, String.class, id);
    LOGGER.info("Retrieved data for ID [" + id + "] from table [" + tableName + "]: " + data);
    return data == null ? "" : data;
  }

}
