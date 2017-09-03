package gov.ehawaii.hacc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
  public List<Grant> getGrants(Map<String, Object> parameters) {
    StringBuffer buffer = new StringBuffer();
    List<Object> arguments = new ArrayList<>();
    for (Entry<String, Object> entry : parameters.entrySet()) {
      buffer.append(entry.getKey());
      buffer.append(" = ?");
      arguments.add(entry.getValue());
    }
    return dao.getGrants(buffer.toString(), arguments.toArray(new String[arguments.size()]));
  }

  @Override
  public List<Grant> getTopFiveGrantsForFiscalYear(String year) {
    if (year == null || year.isEmpty()) {
      throw new IllegalArgumentException("year is null or empty.");
    }

    return dao.findTopFiveGrantsForFiscalYear(Integer.parseInt(year));
  }

  @Override
  public List<Map<String, Object>> getTopNGrants(int top, String field1, String field2) {
    if (field1 == null || field1.isEmpty()) {
      throw new IllegalArgumentException("field is null or empty.");
    }
    if (field2 == null || field2.isEmpty()) {
      throw new IllegalArgumentException("criterion is null or empty.");
    }

    return dao.getTopNGrants(top, field1, field2);
  }

  @Override
  public List<String> getAllOrganizations() {
    return dao.getAllOrganizations();
  }

  @Override
  public List<Map<String, Long>> getOrganizationDataOverTime(String organization, String field) {
    return dao.getOrganizationDataOverTime(organization, field);
  }

}
