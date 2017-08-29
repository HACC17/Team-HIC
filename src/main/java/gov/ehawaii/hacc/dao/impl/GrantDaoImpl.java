package gov.ehawaii.hacc.dao.impl;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import gov.ehawaii.hacc.dao.GrantDao;
import gov.ehawaii.hacc.model.Grant;

@Repository("GrantDao")
public class GrantDaoImpl extends JdbcDaoSupport implements GrantDao {

  private static final Logger LOGGER = LogManager.getLogger(GrantDaoImpl.class);

  @Resource(name = "dataSource")
  private DataSource dataSource;

  @PostConstruct
  void init() {
    setDataSource(dataSource);
    LOGGER.info("GrantDaoImpl initialized.");
  }

  @Override
  public boolean saveGrant(Grant grant) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public List<Grant> findGrantsByFiscalYear(int fiscalYear) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Grant> findGrantsByGrantType(String grantType) {
    // TODO Auto-generated method stub
    return null;
  }

}
