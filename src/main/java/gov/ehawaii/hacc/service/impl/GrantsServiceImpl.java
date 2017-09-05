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
    FILTERS_MAP.put("type", SqlStatements.GRANT_TYPE_ID);
    FILTERS_MAP.put("organization", SqlStatements.ORGANIZATION_ID);
    FILTERS_MAP.put("project", SqlStatements.PROJECT_ID);
    FILTERS_MAP.put("location", SqlStatements.LOCATION_ID);
    FILTERS_MAP.put("priority", SqlStatements.STRATEGIC_PRIORITY_ID);
    FILTERS_MAP.put("result", SqlStatements.STRATEGIC_RESULTS_ID);
    FILTERS_MAP.put("fiscal-gte", SqlStatements.FISCAL_YEAR_GTE);
    FILTERS_MAP.put("fiscal-lte", SqlStatements.FISCAL_YEAR_LTE);
    FILTERS_MAP.put("amount-gte", SqlStatements.AMOUNT_GTE);
    FILTERS_MAP.put("amount-lte", SqlStatements.AMOUNT_LTE);
    FILTERS_MAP.put("total-gte", SqlStatements.TOTAL_NUMBER_SERVED_GTE);
    FILTERS_MAP.put("total-lte", SqlStatements.TOTAL_NUMBER_SERVED_LTE);
    FILTERS_MAP.put("hawaiians-gte", SqlStatements.NUMBER_NATIVE_HAWAIIANS_SERVED_GTE);
    FILTERS_MAP.put("hawaiians-lte", SqlStatements.NUMBER_NATIVE_HAWAIIANS_SERVED_LTE);
  }

  @Autowired
  private GrantsDao dao;

  @Override
  public boolean insertGrant(Grant grant) {
    return dao.saveGrant(grant);
  }

  @Override
  public List<Grant> getGrants(Map<String, Object> filters) {
    StringBuffer buffer = new StringBuffer();
    List<Object> arguments = new ArrayList<>();

    for (Entry<String, Object> entry : filters.entrySet()) {
      if (entry.getValue() != null && !entry.getValue().toString().isEmpty()) {
        String key = FILTERS_MAP.get(entry.getKey());
        if (key == null) {
          throw new IllegalArgumentException(entry.getKey() + " filter not supported, yet.");
        }
        buffer.append(key);

        String value = entry.getValue().toString();
        switch (key) {
        case SqlStatements.GRANT_STATUS_ID:
          arguments.add(dao.getId(SqlStatements.GRANT_STATUSES, SqlStatements.STATUS, value));
          break;
        case SqlStatements.GRANT_TYPE_ID:
          arguments.add(dao.getId(SqlStatements.GRANT_TYPES, SqlStatements.GRANT_TYPE, value));
          break;
        case SqlStatements.ORGANIZATION_ID:
          arguments.add(dao.getId(SqlStatements.ORGANIZATIONS, SqlStatements.ORGANIZATION, value));
          break;
        case SqlStatements.PROJECT_ID:
          arguments.add(dao.getId(SqlStatements.PROJECTS, SqlStatements.PROJECT, value));
          break;
        case SqlStatements.LOCATION_ID:
          arguments.add(dao.getId(SqlStatements.LOCATIONS, SqlStatements.LOCATION, value));
          break;
        case SqlStatements.STRATEGIC_PRIORITY_ID:
          arguments.add(dao.getId(SqlStatements.STRATEGIC_PRIORITIES, SqlStatements.STRATEGIC_PRIORITY, value));
          break;
        case SqlStatements.STRATEGIC_RESULTS_ID:
          arguments.add(dao.getId(SqlStatements.STRATEGIC_RESULTS, SqlStatements.STRATEGIC_RESULT, value));
          break;
        default:
          arguments.add(value);
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
      throw new IllegalArgumentException("field1 is null or empty.");
    }
    if (field2 == null || field2.isEmpty()) {
      throw new IllegalArgumentException("field2 is null or empty.");
    }

    return dao.getTopNGrants(top, field1, field2);
  }

  @Override
  public List<Map<String, Long>> getOrganizationDataOverTime(String organization, String field) {
    return dao.getOrganizationDataOverTime(organization, field);
  }

  @Override
  public Map<String, Map<String, Long>> getDataForEachLocation(String year, String field) {
    return dao.getDataForEachLocation(year, field);
  }

  @Override
  public List<String> getAllGrantStatuses() {
    return dao.getAllGrantStatuses();
  }

  @Override
  public List<String> getAllGrantTypes() {
    return dao.getAllGrantTypes();
  }

  @Override
  public List<String> getAllLocations() {
    return dao.getAllLocations();
  }

  @Override
  public List<String> getAllOrganizations() {
    return dao.getAllOrganizations();
  }

  @Override
  public List<String> getAllProjects() {
    return dao.getAllProjects();
  }

  @Override
  public List<String> getAllStrategicPriorities() {
    return dao.getAllStrategicPriorities();
  }

  @Override
  public List<String> getAllStrategicResults() {
    return dao.getAllStrategicResults();
  }

  @Override
  public List<String> getAllFiscalYears() {
    return dao.getAllFiscalYears();
  }

}
