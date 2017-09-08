package gov.ehawaii.hacc.service;

import java.util.List;
import java.util.Map;
import gov.ehawaii.hacc.model.Grant;

public interface GrantsService {

  boolean saveGrant(Grant grant);

  /**
   * Returns a list of grants that satisfy the given conditions.
   * 
   * @param filters Conditions that must be satisfied.
   * @return A list of grants.
   */
  List<Grant> getGrants(Map<String, Object> filters);

  /**
   * Returns a list of the top 5 organizations for the given fiscal year.
   * 
   * @param year The fiscal year.
   * @return A list of the top 5 organizations.
   */
  List<Map<String, Object>> getTopFiveOrganizationsForFiscalYear(String year);

  /**
   * Returns a list of the top N (N = <code>top</code>) organizations, projects, etc. (<code>field1</code>) by the given criterion (<code>field2</code>).
   * 
   * @param top N, a number greater than 0.
   * @param field1 Organizations, projects, etc.
   * @param field2 The criterion.
   * @return A list of grants of size N.
   */
  List<Map<String, Object>> getTopNData(int top, String field1, String field2);

  List<Map<String, Long>> getOrganizationDataOverTime(String organization, String field);

  Map<String, Map<String, Long>> getAggregateDataForEachLocation(String aggregateField, String filter, String filterValue);

  Map<String, Map<String, Long>> getTopNDataForEachLocation(int top, String aggregateField, Map<String, String> filters);

  /**
   * A list of all grant statuses stored in the repository.
   * 
   * @return A list of grant statuses.
   */
  List<String> getAllGrantStatuses();

  /**
   * A list of all grant types stored in the repository.
   * 
   * @return A list of grant types.
   */
  List<String> getAllGrantTypes();

  /**
   * A list of all locations stored in the repository.
   * 
   * @return A list of locations.
   */
  List<String> getAllLocations();

  /**
   * A list of all organizations stored in the repository.
   * 
   * @return A list of organizations.
   */
  List<String> getAllOrganizations();

  /**
   * A list of all projects stored in the repository.
   * 
   * @return A list of projects.
   */
  List<String> getAllProjects();

  /**
   * A list of all strategic priorities stored in the repository.
   * 
   * @return A list of strategic priorities.
   */
  List<String> getAllStrategicPriorities();

  /**
   * A list of all strategic results stored in the repository.
   * 
   * @return A list of strategic results.
   */
  List<String> getAllStrategicResults();

  /**
   * A list of all fiscal years stored in the repository.
   * 
   * @return A list of fiscal years.
   */
  List<String> getAllFiscalYears();

}
