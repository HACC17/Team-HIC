package gov.ehawaii.hacc.dao;

import java.util.List;
import java.util.Map;
import gov.ehawaii.hacc.model.Grant;
import gov.ehawaii.hacc.specifications.ColumnSpecification;
import gov.ehawaii.hacc.specifications.AggregateDataSpecification;
import gov.ehawaii.hacc.specifications.FilteredGrantsSpecification;
import gov.ehawaii.hacc.specifications.IdSpecification;
import gov.ehawaii.hacc.specifications.OrganizationSpecification;
import gov.ehawaii.hacc.specifications.TopNSpecification;

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
  boolean insertGrant(Grant grant);


  /**
   * Retrieves a list of grants from the database that satisfy the given conditions.
   * 
   * @param specification Contains the conditions.
   * @return A list of grants that satisfy the given conditions.
   */
  List<Grant> findGrants(FilteredGrantsSpecification specification);


  /**
   * Retrieves the top N data (e.g. top 10 organizations by total number of people served).
   * 
   * @param specification Contains the filter that is applied to the query used to retrieve the top N data.
   * @return A map containing the top N data.
   */
  List<Map<String, Object>> findTopN(TopNSpecification specification);


  /**
   * Retrieves the value associated with the given ID from the given table.
   * 
   * @param specification Contains the ID for which to retrieve the value.
   * @return The value, or an empty string if the given ID is not associated with any values.
   */
  String findValueForId(IdSpecification specification);


  /**
   * Retrieves all the data for an organization over a period of time.
   * 
   * @param specification Contains the filter that is applied to the query used to retrieve data for an organization.
   * @return A list containing the data for the given organization over a period of time.
   */
  List<Map<String, Long>> findDataOverTime(OrganizationSpecification specification);


  /**
   * Aggregates all the data for each location and then returns a list of data grouped by grant type. A filter can be
   * added to the given specification, which can then be used in the query for aggregate data.
   * 
   * @param specification Contains the filter that is applied to the query used to retrieve data for each location.
   * @return A map containing all the data for each location for the given fiscal year.
   */
  Map<String, Map<String, Long>> findAggregateDataForEachLocation(AggregateDataSpecification specification);


  /**
   * Retrieves the ID associated with the given value from the given table.
   * 
   * @param specification Contains the value for which to retrieve the ID.
   * @return The ID associated with the given value, or -1 if the ID is not found.
   */
  long findIdForValue(IdSpecification specification);


  /**
   * Retrieves a list of all values found in a column from a table.
   * 
   * @param specification Contains the name of the table and column from which to retrieve values.
   * @return A list of all values found in the column from the table.
   */
  List<String> findAllValues(ColumnSpecification specification);

}
