package gov.ehawaii.hacc.dao;

import java.util.List;
import java.util.Map;
import gov.ehawaii.hacc.model.Grant;

/**
 * Implementations of this interface are responsible for:
 * 
 * <ul>
 * <li>Saving a grant to the database</li>
 * <li>Retrieving a list of grants from the database that satisfy one or more conditions</li>
 * </ul>
 * 
 * Delete operations are currently not supported in this version of the interface.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
public interface GrantsDao {

  /**
   * Saves the given grant to the underlying database.
   * 
   * @param grant The grant to save.
   * @return <code>true</code> if the grant was successfully saved, <code>false</code> otherwise.
   */
  boolean saveGrant(Grant grant);

  /**
   * Retrieves a list of grants from the database that satisfy the given conditions.
   * 
   * @param filter The filter to apply to the query that returns a list of grants.
   * @param arguments The values for the filter.
   * @return A list of grants that satisfy the given conditions.
   */
  List<Grant> getGrants(String filter, Object[] arguments);

  /**
   * Sums the amounts for each organization for the given fiscal year and returns a list of the top
   * five organizations.
   * 
   * @param fiscalYear The fiscal year.
   * @return A list of the top five organizations.
   */
  List<Grant> getTopFiveOrganizationsForFiscalYear(int fiscalYear);

  /**
   * Retrieves the top N (<code>top</code>) organizations, projects, locations, etc.
   * (<code>field1</code>) based on the given data (e.g. amounts, total number of people served, or
   * number of Native Hawaiians served) (<code>field2</code>).
   * 
   * @param top N, a number greater than 0.
   * @param field1 Organization, project, location, etc.
   * @param field2 The type of data (amounts, total number of people served, etc.) to retrieve.
   * @return A map containing the top <code>top</code> organizations or projects.
   */
  List<Map<String, Object>> getTopNGrants(int top, String field1, String field2);

  /**
   * Retrieves the grant status associated with the given ID.
   * 
   * @param grantStatusId The ID for which to retrieve the status.
   * @return The status, or an empty string if the given ID is not associated with any status.
   */
  String getGrantStatusForId(long grantStatusId);

  /**
   * Retrieves the given data (e.g. amounts, total number of people served, or number of Native
   * Hawaiians served) for the given organization for all the fiscal years.
   * 
   * @param organization The organization for which to retrieve the data.
   * @param field The type of data (amounts, total number of people served, etc.) to retrieve.
   * @return A list containing the data for the given organization for all the fiscal years.
   */
  List<Map<String, Long>> getOrganizationDataOverTime(String organization, String field);

  /**
   * Retrieves all the data (e.g. amounts, total number of people served, and number of Native
   * Hawaiians served) for each location (e.g. Oahu, Hawaii, Statewide, etc.) stored in the database
   * for the given fiscal year.
   * 
   * @param year The fiscal year.
   * @param field The type of data (amounts, total number of people served, etc.) to retrieve.
   * @return A map containing all the data for each location for the given year.
   */
  Map<String, Map<String, Long>> getDataForEachLocation(String year, String field);

  /**
   * Retrieves the ID associated with the given value from the given table.
   * 
   * @param tableName The table that contains the given value.
   * @param columnName The column in which the given value is located.
   * @param value The value.
   * @return The ID associated with the given value, or -1 if the ID is not found.
   */
  long getId(String tableName, String columnName, String value);

  /**
   * Retrieves a list of all grant statuses from the database.
   * 
   * @return A list of grant statuses.
   */
  List<String> getAllGrantStatuses();

  /**
   * Retrieves a list of all grant types from the database.
   * 
   * @return A list of grant types.
   */
  List<String> getAllGrantTypes();

  /**
   * Retrieves a list of all locations from the database.
   * 
   * @return A list of locations.
   */
  List<String> getAllLocations();

  /**
   * Retrieves a list of all organizations from the database.
   * 
   * @return A list of organizations.
   */
  List<String> getAllOrganizations();

  /**
   * Retrieves a list of all projects from the database.
   * 
   * @return A list of projects.
   */
  List<String> getAllProjects();

  /**
   * Retrieves a list of strategic priorities from the database.
   * 
   * @return A list of strategic priorities.
   */
  List<String> getAllStrategicPriorities();

  /**
   * Retrieves a list of strategic results from the database.
   * 
   * @return A list of strategic results.
   */
  List<String> getAllStrategicResults();

  /**
   * Retrieves a list of fiscal years from the database.
   * 
   * @return A list of fiscal years.
   */
  List<String> getAllFiscalYears();

}
