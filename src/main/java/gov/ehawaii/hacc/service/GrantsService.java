package gov.ehawaii.hacc.service;

import java.util.List;
import java.util.Map;
import gov.ehawaii.hacc.model.Grant;

public interface GrantsService {

  boolean saveGrant(Grant grant);

  List<Grant> getGrants(Map<String, Object> filters);

  List<Grant> getTopFiveOrganizationsForFiscalYear(String year);

  List<Map<String, Object>> getTopNGrants(int top, String field1, String field2);

  List<Map<String, Long>> getOrganizationDataOverTime(String organization, String field);

  Map<String, Map<String, Long>> getDataForEachLocation(String year, String field);

  List<String> getAllGrantStatuses();

  List<String> getAllGrantTypes();

  List<String> getAllLocations();

  List<String> getAllOrganizations();

  List<String> getAllProjects();

  List<String> getAllStrategicPriorities();

  List<String> getAllStrategicResults();

  List<String> getAllFiscalYears();

}
