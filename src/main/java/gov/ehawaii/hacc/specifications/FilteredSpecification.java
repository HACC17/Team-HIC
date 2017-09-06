package gov.ehawaii.hacc.specifications;

import lombok.Getter;

@Getter
public class FilteredSpecification implements SqlSpecification {

  private final String table;
  private final String filter;
  private final Object[] arguments;

  public FilteredSpecification(String table, String filter, Object[] arguments) {
    this.table = table;
    this.filter = filter;
    this.arguments = arguments == null ? new String[0] : arguments.clone();
  }

  @Override
  public String getTable() {
    return table;
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
    return (filter.contains("?") ? " WHERE " : "") + filter;
  }

  public Object[] getArguments() {
    return arguments.clone();
  }

}
