package gov.ehawaii.hacc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import gov.ehawaii.hacc.model.Grant;
import gov.ehawaii.hacc.repositories.GrantsRepository;
import gov.ehawaii.hacc.repositories.impl.Filters;
import gov.ehawaii.hacc.repositories.impl.SqlStatements;
import gov.ehawaii.hacc.repositories.impl.Tables;
import gov.ehawaii.hacc.service.GrantsService;
import gov.ehawaii.hacc.specifications.AggregateSpecification;
import gov.ehawaii.hacc.specifications.ColumnSpecification;
import gov.ehawaii.hacc.specifications.FilteredSpecification;
import gov.ehawaii.hacc.specifications.IdSpecification;
import gov.ehawaii.hacc.specifications.TimeSeriesSpecification;
import gov.ehawaii.hacc.specifications.TopNFiscalYearSpecification;
import gov.ehawaii.hacc.specifications.TopNSpecification;

@Service
public class GrantsServiceImpl implements GrantsService {

  @Autowired
  private GrantsRepository dao;


  @Override
  public boolean saveGrant(Grant grant) {
    return dao.insertGrant(grant);
  }


  @Override
  public List<Grant> getGrants(Map<String, Object> filters) {
    StringBuffer buffer = new StringBuffer();
    List<Object> arguments = new ArrayList<>();

    for (Entry<String, Object> entry : filters.entrySet()) {
      if (entry.getValue() != null && !entry.getValue().toString().isEmpty()) {
        String key = Filters.FILTERS_MAP.get(entry.getKey());
        if (key == null) {
          throw new IllegalArgumentException(entry.getKey() + " filter not supported, yet.");
        }
        buffer.append(key);

        String value = entry.getValue().toString();
        switch (key) {
        case Filters.GRANT_STATUS_ID_FILTER:
          arguments.add(getId(Tables.GRANT_STATUSES, SqlStatements.STATUS, value));
          break;
        case Filters.GRANT_TYPE_ID_FILTER:
          arguments.add(getId(Tables.GRANT_TYPES, SqlStatements.GRANT_TYPE, value));
          break;
        case Filters.ORGANIZATION_ID_FILTER:
          arguments.add(getId(Tables.ORGANIZATIONS, SqlStatements.ORGANIZATION, value));
          break;
        case Filters.PROJECT_ID_FILTER:
          arguments.add(getId(Tables.PROJECTS, SqlStatements.PROJECT, value));
          break;
        case Filters.LOCATION_ID_FILTER:
          arguments.add(getId(Tables.LOCATIONS, SqlStatements.LOCATION, value));
          break;
        case Filters.STRATEGIC_PRIORITY_ID_FILTER:
          arguments.add(getId(Tables.STRATEGIC_PRIORITIES, SqlStatements.STRATEGIC_PRIORITY, value));
          break;
        case Filters.STRATEGIC_RESULTS_ID_FILTER:
          arguments.add(getId(Tables.STRATEGIC_RESULTS, SqlStatements.STRATEGIC_RESULT, value));
          break;
        default:
          arguments.add(value);
        }

      }
    }
    String filter = buffer.toString().trim().replace(" ? ", " ? AND ");
    Object[] filterValues = arguments.toArray(new Object[arguments.size()]);

    return dao.findGrants(new FilteredSpecification(Tables.GRANTS, filter, filterValues));
  }


  private long getId(String table, String column, String value) {
    return dao.findIdForValue(new IdSpecification(table, column, value));
  }


  @Override
  public List<Map<String, Object>> getTopFiveOrganizationsForFiscalYear(String year) {
    if (year == null || year.isEmpty()) {
      throw new IllegalArgumentException("year is null or empty.");
    }

    return dao.findTopN(new TopNFiscalYearSpecification(5, Tables.GRANTS, SqlStatements.ORGANIZATION_ID,
        SqlStatements.AMOUNT, Integer.parseInt(year)));
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

    return dao.findTopN(new TopNSpecification(top, Tables.GRANTS, field1 + "_ID", field2));
  }


  @Override
  public List<Map<String, Long>> getOrganizationDataOverTime(String organization, String field) {
    if (organization == null || organization.isEmpty()) {
      throw new IllegalArgumentException("organization is null or empty.");
    }
    if (field == null || field.isEmpty()) {
      throw new IllegalArgumentException("field is null or empty.");
    }

    return dao.findTimeSeriesData(
        new TimeSeriesSpecification(Tables.ORGANIZATIONS, SqlStatements.ORGANIZATION, organization,
            SqlStatements.GET_AGGREGATE_DATA_FOR_ORGANIZATION, field));
  }


  @Override
  public Map<String, Map<String, Long>> getAggregateDataForEachLocation(String aggregateField,
      String filter, String filterValue) {
    return dao.findAggregateData(new AggregateSpecification(
        SqlStatements.GET_TOTAL_FOR_EACH_LOCATION, SqlStatements.GET_ALL_DATA_FOR_LOCATION,
        aggregateField, new String[] { filter }, new Object[] { filterValue }));
  }


  @Override
  public Map<String, Map<String, Long>> getTopNDataForEachLocation(int top, String aggregateField, String filter,
      String filterValue) {
    String stmt = SqlStatements.GET_TOP_N_ORGANIZATIONS_FOR_EACH_LOCATION.replace("xxx", aggregateField).replace("yyy", String.valueOf(top));
    return dao.findAggregateData(
        new AggregateSpecification(stmt, null, aggregateField, new String[] { filter }, new Object[] { filterValue }));
  }


  @Override
  public List<String> getAllGrantStatuses() {
    return dao.findAllValues(new ColumnSpecification(Tables.GRANT_STATUSES, SqlStatements.GRANT_STATUS));
  }


  @Override
  public List<String> getAllGrantTypes() {
    return dao.findAllValues(new ColumnSpecification(Tables.GRANT_TYPES, SqlStatements.GRANT_TYPE));
  }


  @Override
  public List<String> getAllLocations() {
    return dao.findAllValues(new ColumnSpecification(Tables.LOCATIONS, SqlStatements.LOCATION));
  }


  @Override
  public List<String> getAllOrganizations() {
    return dao.findAllValues(new ColumnSpecification(Tables.ORGANIZATIONS, SqlStatements.ORGANIZATION));
  }


  @Override
  public List<String> getAllProjects() {
    return dao.findAllValues(new ColumnSpecification(Tables.PROJECTS, SqlStatements.PROJECT));
  }


  @Override
  public List<String> getAllStrategicPriorities() {
    return dao
        .findAllValues(new ColumnSpecification(Tables.STRATEGIC_PRIORITIES, SqlStatements.STRATEGIC_PRIORITY));
  }


  @Override
  public List<String> getAllStrategicResults() {
    return dao.findAllValues(new ColumnSpecification(Tables.STRATEGIC_RESULTS, SqlStatements.STRATEGIC_RESULT));
  }


  @Override
  public List<String> getAllFiscalYears() {
    return dao.findAllValues(new ColumnSpecification(Tables.GRANTS, SqlStatements.FISCAL_YEAR, true));
  }

}
