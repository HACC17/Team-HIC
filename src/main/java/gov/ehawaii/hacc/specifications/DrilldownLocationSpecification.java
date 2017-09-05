package gov.ehawaii.hacc.specifications;

import lombok.Getter;

@Getter
public class DrilldownLocationSpecification implements GrantsSpecification {

  private final String aggregateField;
  private final String filterField;

  public DrilldownLocationSpecification(String aggregateField, String filterField) {
    this.aggregateField = aggregateField;
    this.filterField = filterField;
  }

  @Override
  public String toSqlClause() {
    return null;
  }

}
