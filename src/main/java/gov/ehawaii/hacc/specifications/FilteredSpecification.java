package gov.ehawaii.hacc.specifications;

import lombok.Getter;
import lombok.Setter;

/**
 * This specification contains a filter and values for that filter, which can be used in a query.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
@Getter
public class FilteredSpecification implements SqlSpecification {

  private final String table;
  private final String filter;
  private final Object[] filterValues;
  @Setter
  private ColumnSpecification colSpec;

  /**
   * Creates a new {@link FilteredSpecification}.
   * 
   * @param table The name of the table to which to apply the filter.
   * @param filter The filter that will be used in a query.
   * @param filterValues The values for the filter.
   */
  public FilteredSpecification(final String table, final String filter,
      final Object[] filterValues) {
    this.table = table;
    this.filter = filter;
    this.filterValues = filterValues == null ? new String[0] : filterValues.clone();
  }

  @Override
  public final String getTable() {
    return table;
  }

  @Override
  public final String getColumn() {
    throw new UnsupportedOperationException("This method is not supported.");
  }

  @Override
  public final Object getValue() {
    throw new UnsupportedOperationException("This method is not supported.");
  }

  @Override
  public final String toSqlClause() {
    return filter;
  }

  @Override
  public final Object[] getFilterValues() {
    return filterValues.clone();
  }

}
