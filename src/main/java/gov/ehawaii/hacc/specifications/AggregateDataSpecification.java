package gov.ehawaii.hacc.specifications;

import gov.ehawaii.hacc.dao.impl.Filters;
import lombok.Getter;

@Getter
public class AggregateDataSpecification implements GrantsSpecification {

  private final String totalsQuery;
  private final String allQuery;
  private final String aggregateField;
  private final String[] filters;
  private final String[] filterValues;

  public AggregateDataSpecification(String totalsQuery, String allQuery, String aggregateField,
      String[] filters, String[] filterValues) {
    this.totalsQuery = totalsQuery;
    this.allQuery = allQuery;
    this.aggregateField = aggregateField;
    this.filters = filters == null ? new String[0] : filters.clone();
    this.filterValues = filterValues == null ? new String[0] : filterValues.clone();
  }

  private String buildFilter() {
    StringBuffer buffer = new StringBuffer();
    for (String filter : filters) {
      buffer.append(Filters.FILTERS_MAP.get(filter));
      buffer.append(" AND ");
    }
    String filter = buffer.toString().trim();
    return filter.substring(0, filter.lastIndexOf(" AND"));
  }

  public String[] getFilters() {
    return filters.clone();
  }

  public String[] getFilterValues() {
    return filterValues.clone();
  }

  @Override
  public String toSqlClause() {
    return buildFilter();
  }

}
