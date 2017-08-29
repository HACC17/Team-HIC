package gov.ehawaii.hacc.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
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

  private RowMapper<Grant> ROW_MAPPER = new RowMapper<Grant>() {

    @Override
    public Grant mapRow(ResultSet rs, int rowNum) throws SQLException {
      Grant grant = new Grant();
      grant.setGrantStatus(getData(SqlStatements.GRANT_STATUSES, "STATUS", rs.getLong(2)));
      grant.setFiscalYear(rs.getInt(3));
      grant.setGrantType(getData(SqlStatements.GRANT_TYPES, "GRANT_TYPE", rs.getLong(4)));
      grant.setOrganization(getData(SqlStatements.ORGANIZATIONS, "ORGANIZATION", rs.getLong(5)));
      grant.setProject(getData(SqlStatements.PROJECTS, "PROJECT", rs.getLong(6)));
      grant.setAmount(rs.getInt(7));
      grant.setLocation(rs.getString(8));
      grant.setGrantType(getData(SqlStatements.STRATEGIC_PRIORITIES, "STRATEGIC_PRIORITY", rs.getLong(9)));
      grant.setGrantType(getData(SqlStatements.STRATEGIC_RESULTS, "STRATEGIC_RESULT", rs.getLong(10)));
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
      grantTypeId = saveData(SqlStatements.GRANT_TYPES, "GRANT_TYPE", grant.getGrantType());
    }

    long organizationId = getId(SqlStatements.ORGANIZATIONS, "ORGANIZATION", grant.getOrganization());
    if (organizationId == -1) {
      organizationId = saveData(SqlStatements.ORGANIZATIONS, "ORGANIZATION", grant.getOrganization());
    }

    long projectId = getId(SqlStatements.PROJECTS, "PROJECT", grant.getProject());
    if (projectId == -1) {
      projectId = saveData(SqlStatements.PROJECTS, "PROJECT", grant.getProject());
    }

    long strategicPriorityId =
        getId(SqlStatements.STRATEGIC_PRIORITIES, "STRATEGIC_PRIORITY", grant.getStrategicPriority());
    if (strategicPriorityId == -1) {
      strategicPriorityId =
          saveData(SqlStatements.STRATEGIC_PRIORITIES, "STRATEGIC_PRIORITY", grant.getStrategicPriority());
    }

    long strategicResultId = getId(SqlStatements.STRATEGIC_RESULTS, "STRATEGIC_RESULT", grant.getStrategicResults());
    if (strategicResultId == -1) {
      strategicResultId = saveData(SqlStatements.STRATEGIC_RESULTS, "STRATEGIC_RESULT", grant.getStrategicResults());
    }

    long grantStatusId = getId(SqlStatements.GRANT_STATUSES, "STATUS", grant.getGrantStatus());
    if (grantStatusId == -1) {
      grantStatusId = saveData(SqlStatements.GRANT_STATUSES, "STATUS", grant.getGrantStatus());
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
  public List<Grant> findGrantsByFiscalYear(int fiscalYear) {
    String stmt = String.format(SqlStatements.GET_GRANT_BY, "FISCAL_YEAR");
    List<Grant> grants = getJdbcTemplate().query(stmt, ROW_MAPPER, fiscalYear);
    LOGGER.info("Found " + grants.size() + " grants with fiscal year [" + fiscalYear + "].");
    return grants;
  }


  @Override
  public List<Grant> findGrantsByGrantType(String grantType) {
    String stmt = String.format(SqlStatements.GET_GRANT_BY, "GRANT_TYPE_ID");
    long grantTypeId = getId(SqlStatements.GRANT_TYPES, "GRANT_TYPE", grantType);
    List<Grant> grants = getJdbcTemplate().query(stmt, ROW_MAPPER, grantTypeId);
    LOGGER.info("Found " + grants.size() + " grants with grant type [" + grantType + "].");
    return grants;
  }


  private long getId(String tableName, String columnName, String value) {
    String stmt = String.format(SqlStatements.GET_ID, tableName, columnName);
    Long id = getJdbcTemplate().queryForObject(stmt, Long.class, value);
    LOGGER.info("Retrieved ID for value [" + value + "] from table [" + tableName + "]: " + id);
    return id == null ? -1 : id;
  }


  private long saveData(String tableName, String columnName, String value) {
    String stmt = String.format(SqlStatements.INSERT_INTO, tableName);
    if (getJdbcTemplate().update(stmt, value) > 0) {
      LOGGER.info("Successfully saved value [" + value + "] into table [" + tableName + "].");
      return getId(tableName, columnName, value);
    }
    return -1;
  }


  private String getData(String tableName, String columnName, long id) {
    String stmt = String.format(SqlStatements.GET_ID, tableName, columnName);
    String data = getJdbcTemplate().queryForObject(stmt, String.class, id);
    LOGGER.info("Retrieved data for ID [" + id + "] from table [" + tableName + "]: " + data);
    return data == null ? "" : data;
  }

}
