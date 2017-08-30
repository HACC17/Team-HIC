package gov.ehawaii.hacc.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import gov.ehawaii.hacc.dao.GrantsDao;
import gov.ehawaii.hacc.model.Grant;
import gov.ehawaii.hacc.service.GrantsService;

@Service
public class GrantsServiceImpl implements GrantsService {

  private static final Logger LOGGER = LogManager.getLogger(GrantsServiceImpl.class);

  @Autowired
  private GrantsDao dao;

  @Override
  public boolean insertGrant(Grant grant) {
    return dao.saveGrant(grant);
  }

  @Override
  public List<Grant> findAll() {
    return dao.retrieveAll();
  }

  @Override
  public List<Grant> find(String searchString, String searchBy) {
    switch (searchBy) {
    case "FISCAL_YEAR":
      try {
        return dao.findGrantsByFiscalYear(Integer.parseInt(searchString));
      }
      catch (NumberFormatException nfe) {
        LOGGER.error("Search string is not an integer: " + searchString, nfe);
        return new ArrayList<>();
      }
    default:
      LOGGER.error(searchBy + " not implemented, yet.");
      return new ArrayList<>();
    }
  }

}
