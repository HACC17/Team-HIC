package gov.ehawaii.hacc.specifications;

import lombok.Getter;

@Getter
public class FilteredGrantsSpecification implements GrantsSpecification {

  private final String table;
  private final String filter;
  private final Object[] arguments;

  public FilteredGrantsSpecification(String table, String filter, Object[] arguments) {
    this.table = table;
    this.filter = filter;
    this.arguments = arguments == null ? new String[0] : arguments.clone();
  }

  @Override
  public String toSqlClause() {
    return filter;
  }

}
