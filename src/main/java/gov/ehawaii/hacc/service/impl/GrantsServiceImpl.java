package gov.ehawaii.hacc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

@Service
public class GrantsServiceImpl implements GrantsService {

  private static final Logger LOGGER = LogManager.getLogger(GrantsServiceImpl.class);

  @Autowired
  private GrantsRepository repository;


  @Override
  public boolean saveGrant(Grant grant) {
    return repository.insertGrant(grant);
  }


  @Override
  public List<Grant> getGrants(Map<String, Object> filters) {
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


  private Object getIdForFilterValue(String key, String value) {
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


  private long getId(String table, String column, String value) {
    return repository.findIdForValue(new IdSpecification(table, column, value));
  }


  @Override
  public List<Map<String, Object>> getTopFiveOrganizationsForFiscalYear(String year) {
    if (year == null || year.isEmpty()) {
      throw new IllegalArgumentException("year is null or empty.");
    }

    return repository.findTopN(new TopNFiscalYearSpecification(5, Columns.ORGANIZATION_ID,
        Columns.AMOUNT, Integer.parseInt(year)));
  }


  @Override
  public List<Map<String, Object>> getTopNData(int top, String field1, String field2) {
    if (top < 1) {
      throw new IllegalArgumentException("top must be greater than 0.");
    }
    if (field1 == null || field1.isEmpty()) {
      throw new IllegalArgumentException("field1 is null or empty.");
    }
    if (field2 == null || field2.isEmpty()) {
      throw new IllegalArgumentException("field2 is null or empty.");
    }

    return repository.findTopN(new TopNSpecification(top, field1 + "_ID", field2));
  }


  @Override
  public List<Map<String, Long>> getOrganizationDataOverTime(String organization, String field) {
    if (organization == null || organization.isEmpty()) {
      throw new IllegalArgumentException("organization is null or empty.");
    }
    if (field == null || field.isEmpty()) {
      throw new IllegalArgumentException("field is null or empty.");
    }

    return repository.findTimeSeriesData(
        new TimeSeriesSpecification(Tables.ORGANIZATIONS, Columns.ORGANIZATION, organization,
            SqlStatements.GET_AGGREGATE_DATA_FOR_ORGANIZATION, field));
  }


  @Override
  public Map<String, Map<String, Long>> getAggregateDataForEachLocation(String aggregateField,
      String filter, String filterValue) {
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
  public Map<String, Map<String, Long>> getTopNDataForEachLocation(int top, String aggregateField, Map<String, String> filters) {
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
  public List<String> getAllGrantStatuses() {
    return repository.findAllValues(new ColumnSpecification(Tables.GRANT_STATUSES, Columns.GRANT_STATUS));
  }


  @Override
  public List<String> getAllGrantTypes() {
    return repository.findAllValues(new ColumnSpecification(Tables.GRANT_TYPES, Columns.GRANT_TYPE));
  }


  @Override
  public List<String> getAllLocations() {
    return repository.findAllValues(new ColumnSpecification(Tables.LOCATIONS, Columns.LOCATION));
  }


  @Override
  public List<String> getAllOrganizations() {
    return repository.findAllValues(new ColumnSpecification(Tables.ORGANIZATIONS, Columns.ORGANIZATION));
  }


  @Override
  public List<String> getAllProjects() {
    return repository.findAllValues(new ColumnSpecification(Tables.PROJECTS, Columns.PROJECT));
  }


  @Override
  public List<String> getAllStrategicPriorities() {
    return repository
        .findAllValues(new ColumnSpecification(Tables.STRATEGIC_PRIORITIES, Columns.STRATEGIC_PRIORITY));
  }


  @Override
  public List<String> getAllStrategicResults() {
    return repository.findAllValues(new ColumnSpecification(Tables.STRATEGIC_RESULTS, Columns.STRATEGIC_RESULT));
  }


  @Override
  public List<String> getAllFiscalYears() {
    return repository.findAllValues(new ColumnSpecification(Tables.GRANTS, Columns.FISCAL_YEAR, true));
  }

}
