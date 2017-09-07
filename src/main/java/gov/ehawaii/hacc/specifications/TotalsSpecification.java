package gov.ehawaii.hacc.specifications;

import gov.ehawaii.hacc.repositories.impl.Filters;
import lombok.Getter;
import lombok.Setter;

@Getter
public class TotalsSpecification implements SqlSpecification {

  private final String totalsQuery;
  private final String aggregateField;
  private final String[] filters;
  private final Object[] filterValues;
  @Setter
  private ColumnSpecification colSpec;

  public TotalsSpecification(String totalsQuery, String aggregateField,
      String[] filters, Object[] filterValues) {
    this.totalsQuery = totalsQuery;
    this.aggregateField = aggregateField;
    this.filters = filters == null ? new String[0] : filters.clone();
    this.filterValues = filterValues == null ? new String[0] : filterValues.clone();
  }

  @Override
  public String getTable() {
    throw new UnsupportedOperationException("This method is not supported.");
  }

  @Override
  public String getColumn() {
    throw new UnsupportedOperationException("This method is not supported.");
  }

  @Override
  public Object getValue() {
    throw new UnsupportedOperationException("This method is not supported.");
  }

  @Override
  public String toSqlClause() {
    return String.format(totalsQuery, aggregateField, buildFilter());
  }

  public String[] getFilters() {
    return filters.clone();
  }

  public Object[] getFilterValues() {
    return filterValues.clone();
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

}
