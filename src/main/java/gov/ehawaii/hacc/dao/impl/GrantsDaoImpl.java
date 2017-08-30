package gov.ehawaii.hacc.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
      return createGrant(rs);
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

    long organizationId = getId(SqlStatements.ORGANIZATIONS, "ORGANIZATION", grant.getOrganization());
    if (organizationId == -1) {
      organizationId = saveValue(SqlStatements.ORGANIZATIONS, "ORGANIZATION", grant.getOrganization());
    }

    long projectId = getId(SqlStatements.PROJECTS, "PROJECT", grant.getProject());
    if (projectId == -1) {
      projectId = saveValue(SqlStatements.PROJECTS, "PROJECT", grant.getProject());
    }

    long strategicPriorityId =
        getId(SqlStatements.STRATEGIC_PRIORITIES, "STRATEGIC_PRIORITY", grant.getStrategicPriority());
    if (strategicPriorityId == -1) {
      strategicPriorityId =
          saveValue(SqlStatements.STRATEGIC_PRIORITIES, "STRATEGIC_PRIORITY", grant.getStrategicPriority());
    }

    long strategicResultId = getId(SqlStatements.STRATEGIC_RESULTS, "STRATEGIC_RESULT", grant.getStrategicResults());
    if (strategicResultId == -1) {
      strategicResultId = saveValue(SqlStatements.STRATEGIC_RESULTS, "STRATEGIC_RESULT", grant.getStrategicResults());
    }

    long grantStatusId = getId(SqlStatements.GRANT_STATUSES, "STATUS", grant.getGrantStatus());
    if (grantStatusId == -1) {
      grantStatusId = saveValue(SqlStatements.GRANT_STATUSES, "STATUS", grant.getGrantStatus());
    }

    long rows = getJdbcTemplate().update(SqlStatements.INSERT_GRANT, grantStatusId, grant.getFiscalYear(), grantTypeId,
        organizationId, projectId, grant.getAmount(), grant.getLocation(), strategicPriorityId, strategicResultId,
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
  public List<Grant> retrieveAll() {
    Long count = getJdbcTemplate().queryForObject("SELECT COUNT(*) FROM GRANTS", Long.class);
    List<Grant> grants = new ArrayList<>();
    if (count > 1) {
      grants = getJdbcTemplate().query(SqlStatements.GET_ALL_GRANTS, rowMapper);
    }
    else {
      grants.add(getJdbcTemplate().queryForObject(SqlStatements.GET_ALL_GRANTS, new Object[0], rowMapper));
    }
    LOGGER.info("Found " + grants.size() + " grant(s).");
    return grants;
  }


  private Grant createGrant(ResultSet rs) throws SQLException {
    Grant grant = new Grant();
    if (rs.next()) {
      grant.setGrantStatus(getValue(SqlStatements.GRANT_STATUSES, "STATUS", rs.getLong(2)));
      grant.setFiscalYear(rs.getInt(3));
      grant.setGrantType(getValue(SqlStatements.GRANT_TYPES, "GRANT_TYPE", rs.getLong(4)));
      grant.setOrganization(getValue(SqlStatements.ORGANIZATIONS, "ORGANIZATION", rs.getLong(5)));
      grant.setProject(getValue(SqlStatements.PROJECTS, "PROJECT", rs.getLong(6)));
      grant.setAmount(rs.getInt(7));
      grant.setLocation(rs.getString(8));
      grant.setGrantType(getValue(SqlStatements.STRATEGIC_PRIORITIES, "STRATEGIC_PRIORITY", rs.getLong(9)));
      grant.setGrantType(getValue(SqlStatements.STRATEGIC_RESULTS, "STRATEGIC_RESULT", rs.getLong(10)));
      grant.setTotalNumberServed(rs.getInt(11));
      grant.setNumberNativeHawaiiansServed(rs.getInt(12));
    }
    return grant;
  }


  @Override
  public List<Grant> findGrantsByFiscalYear(int fiscalYear) {
    List<Grant> grants = getGrantsBy("FISCAL_YEAR", fiscalYear);
    LOGGER.info("Found " + grants.size() + " grant(s) with fiscal year [" + fiscalYear + "].");
    return grants;
  }


  @Override
  public List<Grant> findGrantsByGrantType(String grantType) {
    long grantTypeId = getId(SqlStatements.GRANT_TYPES, "GRANT_TYPE", grantType);
    List<Grant> grants = getGrantsBy("GRANT_TYPE_ID", grantTypeId);
    LOGGER.info("Found " + grants.size() + " grant(s) with grant type [" + grantType + "].");
    return grants;
  }


  private List<Grant> getGrantsBy(String columnName, Object columnValue) {
    String countStmt = String.format(SqlStatements.COUNT, "GRANTS", columnName);
    Long count = getJdbcTemplate().queryForObject(countStmt, Long.class, columnValue);

    String select = String.format(SqlStatements.GET_GRANT_BY, columnName);
    if (count > 1) {
      return getJdbcTemplate().query(select, rowMapper, columnValue);
    }
    else {
      List<Grant> grants = new ArrayList<>();
      grants.add(getJdbcTemplate().queryForObject(select, new Object[] { columnValue }, rowMapper));
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
    return id;
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
    String stmt = String.format(SqlStatements.GET_ID, tableName, columnName);
    String data = getJdbcTemplate().queryForObject(stmt, String.class, id);
    LOGGER.info("Retrieved data for ID [" + id + "] from table [" + tableName + "]: " + data);
    return data == null ? "" : data;
  }

}
