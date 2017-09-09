package gov.ehawaii.hacc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import gov.ehawaii.hacc.model.Grant;
import gov.ehawaii.hacc.repositories.GrantsRepository;
import gov.ehawaii.hacc.repositories.impl.Columns;
import gov.ehawaii.hacc.repositories.impl.Filters;
import gov.ehawaii.hacc.repositories.impl.SqlStatements;
import gov.ehawaii.hacc.repositories.impl.Tables;
import gov.ehawaii.hacc.service.GrantsService;
import gov.ehawaii.hacc.specifications.ColumnSpecification;
import gov.ehawaii.hacc.specifications.FilteredSpecification;
import gov.ehawaii.hacc.specifications.IdSpecification;
import gov.ehawaii.hacc.specifications.TimeSeriesSpecification;
import gov.ehawaii.hacc.specifications.TopNFiscalYearSpecification;
import gov.ehawaii.hacc.specifications.TopNSpecification;
import gov.ehawaii.hacc.specifications.TotalsSpecification;

/**
 * This service will save grants to and retrieve grants from a {@link GrantsRepository}. Methods in this
 * class will create one or more {@link Specification}s that will be passed to the repository and used
 * to fetch grants from it.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
@Service
public class GrantsServiceImpl implements GrantsService {

  private static final Logger LOGGER = LogManager.getLogger(GrantsServiceImpl.class);

  @Autowired
  private GrantsRepository repository;


  @Override
  public final boolean saveGrant(final Grant grant) {
    return repository.insertGrant(grant);
  }


  @Override
  public final List<Grant> getGrants(final Map<String, Object> filters) {
    StringBuffer buffer = new StringBuffer();
    List<Object> arguments = new ArrayList<>();

    for (Entry<String, Object> entry : filters.entrySet()) {
      String key = entry.getKey();
      Object obj = entry.getValue();

      if (!(obj instanceof ArrayList)) {
        String errorMessage = "Expected list of values, but type was " + obj.getClass() + ".";
        throw new IllegalArgumentException(errorMessage);
      }

      @SuppressWarnings("unchecked")
      ArrayList<String> filterValues = (ArrayList<String>) obj;
      if (!filterValues.isEmpty()) {
        String filter = Filters.FILTERS_MAP.get(key);
        if (filter == null) {
          throw new IllegalArgumentException(key + " filter not supported, yet.");
        }
        buffer.append("(");
        for (String value : filterValues) {
          buffer.append(filter);
          buffer.append("OR ");
          arguments.add(getIdForFilterValue(filter, value));
        }
        buffer.append(")");
      }
    }

    String filter = buffer.toString().replace(" OR )", ") AND ").trim();
    filter = filter.substring(0, filter.lastIndexOf(")") + 1);
    filter = (filter.contains("?") ? " WHERE " : "") + filter;
    LOGGER.info("Filter: " + filter);
    Object[] filterValues = arguments.toArray(new Object[arguments.size()]);

    return repository.findGrants(new FilteredSpecification(Tables.GRANTS, filter, filterValues));
  }


  /**
   * Gets the ID for the given value. When querying the GRANTS table, the ID is needed for some columns.
   * 
   * @param key A key from the filters map, which can be found in the Filters class.
   * @param value The value for which to get the ID.
   * @return The ID, -1 if the value does not have an ID associated with it, or the value if the key cannot be found in the filters map.
   */
  private Object getIdForFilterValue(final String key, final String value) {
    switch (key) {
    case Filters.GRANT_STATUS_ID_FILTER:
      return getId(Tables.GRANT_STATUSES, Columns.GRANT_STATUS, value);
    case Filters.GRANT_TYPE_ID_FILTER:
      return getId(Tables.GRANT_TYPES, Columns.GRANT_TYPE, value);
    case Filters.ORGANIZATION_ID_FILTER:
      return getId(Tables.ORGANIZATIONS, Columns.ORGANIZATION, value);
    case Filters.PROJECT_ID_FILTER:
      return getId(Tables.PROJECTS, Columns.PROJECT, value);
    case Filters.LOCATION_ID_FILTER:
      return getId(Tables.LOCATIONS, Columns.LOCATION, value);
    case Filters.STRATEGIC_PRIORITY_ID_FILTER:
      return getId(Tables.STRATEGIC_PRIORITIES, Columns.STRATEGIC_PRIORITY, value);
    case Filters.STRATEGIC_RESULTS_ID_FILTER:
      return getId(Tables.STRATEGIC_RESULTS, Columns.STRATEGIC_RESULT, value);
    default:
      return value;
    }
  }


  /**
   * Gets the ID for the given value from the given table.
   * 
   * @param table The table from which to get the ID.
   * @param column The column that contains the given value.
   * @param value The value for which to get the ID.
   * @return The ID, or -1 if the value does not have an ID associated with it.
   */
  private long getId(final String table, final String column, final String value) {
    return repository.findIdForValue(new IdSpecification(table, column, value));
  }


  @Override
  public final List<Map<String, Object>> getTopFiveOrganizationsForFiscalYear(final String year) {
    Assert.hasLength(year, "year must not be null or empty.");

    return repository.findTopN(new TopNFiscalYearSpecification(5, Columns.ORGANIZATION_ID,
        Columns.AMOUNT, Integer.parseInt(year)));
  }


  @Override
  public final List<Map<String, Object>> getTopNData(final int top, final String name, final String aggregateField) {
    if (top < 1) {
      throw new IllegalArgumentException("top must be greater than 0.");
    }
    Assert.hasLength(name, "name must not be null or empty.");
    Assert.hasLength(aggregateField, "aggregateField must not be null or empty.");

    return repository.findTopN(new TopNSpecification(top, name + "_ID", aggregateField));
  }


  @Override
  public final List<Map<String, Long>> getOrganizationDataOverTime(final String organization, final String field) {
    Assert.hasLength(organization, "organization must not be null or empty.");
    Assert.hasLength(field, "field must not be null or empty.");

    return repository.findTimeSeriesData(
        new TimeSeriesSpecification(Tables.ORGANIZATIONS, Columns.ORGANIZATION, organization,
            SqlStatements.GET_AGGREGATE_DATA_FOR_ORGANIZATION, field));
  }


  @Override
  public final Map<String, Map<String, Long>> getAggregateDataForEachLocation(final String aggregateField,
      final String filter, final String filterValue) {
    TotalsSpecification totalsSpecification = new TotalsSpecification(
        SqlStatements.GET_TOTAL_FOR_EACH_LOCATION,
        aggregateField, new String[] { filter }, new Object[] { filterValue });
    Map<String, Map<String, Long>> data = repository.findAggregateData(totalsSpecification);

    totalsSpecification = new TotalsSpecification(
        SqlStatements.GET_ALL_DATA_FOR_LOCATION,
        aggregateField, new String[] { filter }, new Object[] { filterValue });
    totalsSpecification.setColSpec(new ColumnSpecification(Tables.LOCATIONS, Columns.LOCATION));
    data.putAll(repository.findAggregateData(totalsSpecification));
    return data;
  }


  @Override
  public final Map<String, Map<String, Long>> getTopNDataForEachLocation(final int top, final String aggregateField, final Map<String, String> filters) {
    String stmt = SqlStatements.GET_TOP_N_ORGANIZATIONS_FOR_EACH_LOCATION.replace("xxx", aggregateField).replace("yyy",
        String.valueOf(top));
    String[] filtersArray = new String[filters.size()];
    Object[] filterValuesArray = new Object[filters.size()];

    int index = 0;
    for (Entry<String, String> entry : filters.entrySet()) {
      String key = entry.getKey();
      filtersArray[index] = key;

      String filterKey = Filters.FILTERS_MAP.get(key);
      if (filterKey == null) {
        throw new IllegalArgumentException(key + " filter not supported, yet.");
      }
      filterValuesArray[index++] = getIdForFilterValue(filterKey, entry.getValue());
    }

    return repository.findAggregateData(new TotalsSpecification(stmt, aggregateField, filtersArray, filterValuesArray));
  }


  @Override
  public final List<String> getAllGrantStatuses() {
    return repository.findAllValues(new ColumnSpecification(Tables.GRANT_STATUSES, Columns.GRANT_STATUS));
  }


  @Override
  public final List<String> getAllGrantTypes() {
    return repository.findAllValues(new ColumnSpecification(Tables.GRANT_TYPES, Columns.GRANT_TYPE));
  }


  @Override
  public final List<String> getAllLocations() {
    return repository.findAllValues(new ColumnSpecification(Tables.LOCATIONS, Columns.LOCATION));
  }


  @Override
  public final List<String> getAllOrganizations() {
    return repository.findAllValues(new ColumnSpecification(Tables.ORGANIZATIONS, Columns.ORGANIZATION));
  }


  @Override
  public final List<String> getAllProjects() {
    return repository.findAllValues(new ColumnSpecification(Tables.PROJECTS, Columns.PROJECT));
  }


  @Override
  public final List<String> getAllStrategicPriorities() {
    return repository
        .findAllValues(new ColumnSpecification(Tables.STRATEGIC_PRIORITIES, Columns.STRATEGIC_PRIORITY));
  }


  @Override
  public final List<String> getAllStrategicResults() {
    return repository.findAllValues(new ColumnSpecification(Tables.STRATEGIC_RESULTS, Columns.STRATEGIC_RESULT));
  }


  @Override
  public final List<String> getAllFiscalYears() {
    return repository.findAllValues(new ColumnSpecification(Tables.GRANTS, Columns.FISCAL_YEAR, true));
  }

}
