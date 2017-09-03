package gov.ehawaii.hacc.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import gov.ehawaii.hacc.dao.GrantsDao;
import gov.ehawaii.hacc.dao.impl.SqlStatements;
import gov.ehawaii.hacc.model.Grant;
import gov.ehawaii.hacc.service.GrantsService;

@Service
public class GrantsServiceImpl implements GrantsService {

  private static final Map<String, String> FILTERS_MAP = new HashMap<>();
  static {
    FILTERS_MAP.put("status", SqlStatements.GRANT_STATUS_ID);
    FILTERS_MAP.put("organization", SqlStatements.ORGANIZATION_ID);
    FILTERS_MAP.put("amount-gte", SqlStatements.AMOUNT_GTE);
    FILTERS_MAP.put("amount-lte", SqlStatements.AMOUNT_LTE);
    FILTERS_MAP.put("fiscalYear", SqlStatements.FISCAL_YEAR);
  }

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
      if (entry.getValue() != null && !entry.getValue().toString().isEmpty()) {
        String key = FILTERS_MAP.get(entry.getKey());
        if (key == null) {
          throw new IllegalArgumentException(entry.getKey() + " filter not supported, yet.");
        }
        buffer.append(key);

        switch (key) {
        case SqlStatements.GRANT_STATUS_ID:
          arguments.add(dao.getId(SqlStatements.GRANT_STATUSES,
              SqlStatements.STATUS, entry.getValue().toString()));
          break;
        case SqlStatements.ORGANIZATION_ID:
          arguments.add(dao.getId(SqlStatements.ORGANIZATIONS,
              SqlStatements.ORGANIZATION, entry.getValue().toString()));
          break;
        default:
          arguments.add(entry.getValue());
        }

      }
    }
    String stmt = buffer.toString().trim().replace(" ? ", " ? AND ");

    return dao.getGrants(stmt, arguments.toArray(new Object[arguments.size()]));
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
  public List<String> getAllStatuses() {
    return dao.getAllStatuses();
  }

  @Override
  public List<Map<String, Long>> getOrganizationDataOverTime(String organization, String field) {
    return dao.getOrganizationDataOverTime(organization, field);
  }

  @Override
  public List<String> getAllStrategicPriorities() {
    return dao.getAllStrategicPriorities();
  }

  @Override
  public List<String> getAllStrategicResults() {
    return dao.getAllStrategicResults();
  }

}
