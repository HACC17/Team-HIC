package gov.ehawaii.hacc.specifications;

import lombok.Getter;

@Getter
public class DrilldownLocationSpecification implements GrantsSpecification {

  private final String aggregateField;
  private final String filter;
  private final String filterValue;

  public DrilldownLocationSpecification(String aggregateField, String filter, String filterValue) {
    this.aggregateField = aggregateField;
    this.filter = filter;
    this.filterValue = filterValue;
  }

  @Override
  public String toSqlClause() {
    return null;
  }

}
