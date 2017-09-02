package gov.ehawaii.hacc.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import gov.ehawaii.hacc.dao.GrantsDao;
import gov.ehawaii.hacc.model.Grant;
import gov.ehawaii.hacc.service.GrantsService;

@Service
public class GrantsServiceImpl implements GrantsService {

  @Autowired
  private GrantsDao dao;

  @Override
  public boolean insertGrant(Grant grant) {
    return dao.saveGrant(grant);
  }

  @Override
  public List<Grant> getAllGrants() {
    return dao.getAllGrants();
  }

  @Override
  public List<Grant> getGrantsForFiscalYear(String year) {
    if (year == null || year.isEmpty()) {
      throw new IllegalArgumentException("year is null or empty.");
    }

    return dao.findGrantsByFiscalYear(Integer.parseInt(year));
  }

  @Override
  public List<Map<String, Object>> getTopNData(int top, String field, String criterion) {
    if (field == null || field.isEmpty()) {
      throw new IllegalArgumentException("field is null or empty.");
    }
    if (criterion == null || criterion.isEmpty()) {
      throw new IllegalArgumentException("criterion is null or empty.");
    }

    return dao.getTop(top, field, criterion);
  }

  @Override
  public List<String> getAllOrganizations() {
    return dao.getAllOrganizations();
  }

  @Override
  public List<Map<String, Long>> getOrganizationDataOverTime(String organization,
      String criterion) {
    return dao.getOrganizationDataOverTime(organization, criterion);
  }

}
