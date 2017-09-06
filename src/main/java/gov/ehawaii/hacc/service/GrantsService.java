package gov.ehawaii.hacc.service;

import java.util.List;
import java.util.Map;
import gov.ehawaii.hacc.model.Grant;

public interface GrantsService {

  boolean saveGrant(Grant grant);

  List<Grant> getGrants(Map<String, Object> filters);

  List<Map<String, Object>> getTopFiveOrganizationsForFiscalYear(String year);

  List<Map<String, Object>> getTopNData(int top, String field1, String field2);

  List<Map<String, Long>> getOrganizationDataOverTime(String organization, String field);

  Map<String, Map<String, Long>> getAggregateDataForEachLocation(String aggregateField, String filter, String filterValue);

  Map<String, Map<String, Long>> getTopNDataForEachLocation(int top, String aggregateField, String filter, String filterValue);

  List<String> getAllGrantStatuses();

  List<String> getAllGrantTypes();

  List<String> getAllLocations();

  List<String> getAllOrganizations();

  List<String> getAllProjects();

  List<String> getAllStrategicPriorities();

  List<String> getAllStrategicResults();

  List<String> getAllFiscalYears();

}
