package gov.ehawaii.hacc.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import gov.ehawaii.hacc.model.Grant;
import gov.ehawaii.hacc.repositories.GrantsRepository;
import gov.ehawaii.hacc.repositories.impl.Columns;
import gov.ehawaii.hacc.repositories.impl.Filters;
import gov.ehawaii.hacc.repositories.impl.SqlStatements;
import gov.ehawaii.hacc.repositories.impl.Tables;
import gov.ehawaii.hacc.repositories.impl.TimeSeries;
import gov.ehawaii.hacc.service.GrantsService;
import gov.ehawaii.hacc.specifications.ColumnSpecification;
import gov.ehawaii.hacc.specifications.FilteredSpecification;
import gov.ehawaii.hacc.specifications.IdSpecification;
import gov.ehawaii.hacc.specifications.TimeSeriesSpecification;
import gov.ehawaii.hacc.specifications.TopNFiscalYearSpecification;
import gov.ehawaii.hacc.specifications.TopNSpecification;
import gov.ehawaii.hacc.specifications.TotalsSpecification;

/**
 * This service will save grants to and retrieve grants from a {@link GrantsRepository}. Methods in
 * this class will create one or more {@link Specification}s that will be passed to the repository
 * and used to fetch grants from it.
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
  public final boolean addNewOrganization(final String organization) {
    return repository.insertOrganization(organization);
  }

  @Override
  public final List<Grant> getGrants(final Map<String, Object> filters) {
    List<Object> arguments = new ArrayList<>();
    String filter = getFilter(filters, arguments);
    LOGGER.debug("Filter: " + filter.trim() + " -- Arguments: " + arguments);
    Object[] filterValues = arguments.toArray(new Object[arguments.size()]);

    return repository.findGrants(new FilteredSpecification(Tables.GRANTS, filter, filterValues));
  }

  /**
   * Creates a filter string given the map of filters and also adds values for the filters to the
   * given list.
   * 
   * @param filters The map of filters.
   * @param arguments The list to which to add values for the filters.
   * @return A string containing all the filters in the map.
   */
  private String getFilter(final Map<String, Object> filters, final List<Object> arguments) {
    StringBuffer buffer = new StringBuffer();

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
          arguments.add(getIdForFilterValue(filter, StringEscapeUtils.unescapeHtml(value)));
        }
        buffer.append(")");
      }
    }

    String filter = buffer.toString().replace(" OR )", ") AND ").trim();
    filter = filter.substring(0, filter.lastIndexOf(")") + 1);
    filter = (filter.contains("?") ? " WHERE " : "") + filter;
    return filter;
  }

  /**
   * Gets the ID for the given value. When querying the GRANTS table, the ID is needed for some
   * columns.
   * 
   * @param key A key from the filters map, which can be found in the Filters class.
   * @param value The value for which to get the ID.
   * @return The ID, -1 if the value does not have an ID associated with it, or the value if the key
   * cannot be found in the filters map.
   */
  private Object getIdForFilterValue(final String key, final String value) {
    switch (key) {
    case Filters.GRANT_STATUS_ID_FILTER:
      return getId(Tables.GRANT_STATUSES, Columns.GRANT_STATUS, value);
    case Filters.GRANT_TYPE_ID_FILTER:
      return getId(Tables.GRANT_TYPES, Columns.GRANT_TYPE, value);
    case Filters.ORGANIZATION_ID_FILTER:
      return getId(Tables.ORGANIZATIONS, Columns.ORGANIZATION, value);
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
  public final List<Map<String, Object>> getTopFiveOrganizationsForFiscalYear(final int year) {
    return repository.findTopN(new TopNFiscalYearSpecification(5, Columns.ORGANIZATION_ID,
        Columns.AMOUNT, year));
  }

  @Override
  public final List<Map<String, Object>> getTopNData(final int top, final String name,
      final String aggregateField) {
    if (top < 1) {
      throw new IllegalArgumentException("top must be greater than 0.");
    }
    Assert.hasLength(name, "name must not be null or empty.");
    Assert.hasLength(aggregateField, "aggregateField must not be null or empty.");

    return repository.findTopN(new TopNSpecification(top, name + "_ID", aggregateField));
  }

  @Override
  public final List<TimeSeries> getTimeSeriesData(final int top, final String field, final Map<String, String> filters) {
    Assert.hasLength(field, "field must not be null or empty.");

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

    return repository.findTimeSeriesData(new TimeSeriesSpecification(Tables.ORGANIZATIONS,
        Columns.ORGANIZATION, top, SqlStatements.GET_AGGREGATE_DATA_FOR_ORGANIZATIONS,
        SqlStatements.GET_TIME_SERIES_DATA_FOR_ORGANIZATIONS, field, filtersArray, filterValuesArray));
  }

  @SuppressWarnings("unchecked")
  @Override
  public final Map<String, Map<String, Long>> getAggregateData(final String name,
      final String aggregateField, final String drilldown, final Map<String, Object> filtersMap) {
    List<String> filtersList = (ArrayList<String>) filtersMap.get("filters");
    String[] filtersArray =
        filtersList == null ? new String[0] : filtersList.toArray(new String[filtersList.size()]);

    List<Object> filterValuesList = (ArrayList<Object>) filtersMap.get("filterValues");
    if (filterValuesList == null) {
      filterValuesList = new ArrayList<>();
    }

    Map<String, Object> filtersListsMap = new HashMap<>();
    int index = 0;
    for (String filter : filtersArray) {
      filtersList = (List<String>) filtersListsMap.get(filter);
      if (filtersList == null) {
        filtersList = new ArrayList<>();
        filtersListsMap.put(filter, filtersList);
      }
      filtersList.add(filterValuesList.get(index++).toString());
    }

    String[] parameters = getTableAndColumnForQuery(name);
    String tTable = parameters[0];
    String tColumn = parameters[1];
    String fkColumn = parameters[2];

    List<Object> arguments = new ArrayList<>();
    String filter = getFilter(filtersListsMap, arguments);

    String stmt = "SELECT T." + tColumn + ", SUM(G." + aggregateField + ") ";
    stmt += "FROM GRANTS G LEFT JOIN " + tTable + " T ON T.ID = G." + fkColumn;
    stmt += (filterValuesList.isEmpty() ? " " : filter + " ");
    stmt += "GROUP BY " + tColumn;

    LOGGER.debug("SQL Statement: " + stmt);

    FilteredSpecification filteredSpec =
        new FilteredSpecification(null, stmt, arguments.toArray(new Object[arguments.size()]));
    Map<String, Map<String, Long>> data = repository.findAggregateData(filteredSpec);

    if (StringUtils.isEmpty(drilldown)) {
      return data;
    }

    parameters = getTableAndColumnForQuery(drilldown);
    String ddTable = parameters[0];
    String ddColumn = parameters[1];

    String drilldownStmt =
        "SELECT " + ddTable + "." + ddColumn + ", SUM(GRANTS." + aggregateField + ") ";
    drilldownStmt +=
        "FROM GRANTS LEFT JOIN " + ddTable + " ON " + ddTable + ".ID = GRANTS." + parameters[2];
    drilldownStmt += " WHERE GRANTS." + fkColumn + " = ? AND";
    drilldownStmt += filter.replace(" WHERE ", " ");
    drilldownStmt += " GROUP BY " + ddColumn;

    LOGGER.debug("SQL Statement: " + drilldownStmt);

    filteredSpec = new FilteredSpecification(null, drilldownStmt,
        arguments.toArray(new Object[arguments.size()]));
    filteredSpec.setColSpec(new ColumnSpecification(tTable, tColumn));
    data.putAll(repository.findAggregateData(filteredSpec));
    return data;
  }

  /**
   * Given a key that exists in the filters map that is found in the {@link Filters} class, this
   * method will return an array of size 3 that contains: the name of the table to which the foreign
   * key column in the filter refers, a column in that table that describes the foreign key, and the
   * foreign key column itself.<br />
   * <br />
   * An {@link IllegalArgumentException} will be thrown if the key cannot be found in the map.
   * 
   * @param key A key in the filters map.
   * @return An array of size 3.
   */
  private static String[] getTableAndColumnForQuery(final String key) {
    String filter = Filters.FILTERS_MAP.get(key);
    switch (filter) {
    case Filters.GRANT_STATUS_ID_FILTER:
      return new String[] { Tables.GRANT_STATUSES, Columns.GRANT_STATUS, Columns.GRANT_STATUS_ID };
    case Filters.GRANT_TYPE_ID_FILTER:
      return new String[] { Tables.GRANT_TYPES, Columns.GRANT_TYPE, Columns.GRANT_TYPE_ID };
    case Filters.ORGANIZATION_ID_FILTER:
      return new String[] { Tables.ORGANIZATIONS, Columns.ORGANIZATION, Columns.ORGANIZATION_ID };
    case Filters.LOCATION_ID_FILTER:
      return new String[] { Tables.LOCATIONS, Columns.LOCATION, Columns.LOCATION_ID };
    case Filters.STRATEGIC_PRIORITY_ID_FILTER:
      return new String[] { Tables.STRATEGIC_PRIORITIES, Columns.STRATEGIC_PRIORITY,
          Columns.STRATEGIC_PRIORITY_ID };
    case Filters.STRATEGIC_RESULTS_ID_FILTER:
      return new String[] { Tables.STRATEGIC_RESULTS, Columns.STRATEGIC_RESULT,
          Columns.STRATEGIC_RESULT_ID };
    default:
      throw new IllegalArgumentException(filter + " filter not supported, yet.");
    }
  }

  @Override
  public final Map<String, Map<String, Long>> getTopNDataForEachLocation(final int top,
      final String aggregateField, final Map<String, String> filters) {
    String stmt = SqlStatements.GET_TOP_N_ORGANIZATIONS_FOR_EACH_LOCATION
        .replace("xxx", aggregateField).replace("yyy", String.valueOf(top));
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

    return repository.findAggregateData(
        new TotalsSpecification(stmt, aggregateField, filtersArray, filterValuesArray));
  }

  @Override
  public final List<String> getAllGrantStatuses() {
    return repository
        .findAllValues(new ColumnSpecification(Tables.GRANT_STATUSES, Columns.GRANT_STATUS));
  }

  @Override
  public final List<String> getAllGrantTypes() {
    return repository
        .findAllValues(new ColumnSpecification(Tables.GRANT_TYPES, Columns.GRANT_TYPE));
  }

  @Override
  public final List<String> getAllLocations() {
    return repository.findAllValues(new ColumnSpecification(Tables.LOCATIONS, Columns.LOCATION));
  }

  @Override
  public final List<String> getAllOrganizations() {
    return repository
        .findAllValues(new ColumnSpecification(Tables.ORGANIZATIONS, Columns.ORGANIZATION));
  }

  @Override
  public final List<String> getAllStrategicPriorities() {
    return repository.findAllValues(
        new ColumnSpecification(Tables.STRATEGIC_PRIORITIES, Columns.STRATEGIC_PRIORITY));
  }

  @Override
  public final List<String> getAllStrategicResults() {
    return repository
        .findAllValues(new ColumnSpecification(Tables.STRATEGIC_RESULTS, Columns.STRATEGIC_RESULT));
  }

  @Override
  public final List<String> getAllFiscalYears() {
    return repository
        .findAllValues(new ColumnSpecification(Tables.GRANTS, Columns.FISCAL_YEAR, true));
  }

}
