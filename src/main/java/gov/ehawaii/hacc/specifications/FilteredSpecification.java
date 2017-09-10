package gov.ehawaii.hacc.specifications;

import lombok.Getter;

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
  private final Object[] arguments;

  /**
   * Creates a new {@link FilteredSpecification}.
   * 
   * @param table The name of the table to which to apply the filter.
   * @param filter The filter that will be used in a query.
   * @param arguments The values for the filter.
   */
  public FilteredSpecification(final String table, final String filter, final Object[] arguments) {
    this.table = table;
    this.filter = filter;
    this.arguments = arguments == null ? new String[0] : arguments.clone();
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

  /**
   * Returns an array of values for the filter.
   * 
   * @return An array of values for the filter.
   */
  public final Object[] getArguments() {
    return arguments.clone();
  }

}
