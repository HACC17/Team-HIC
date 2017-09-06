package gov.ehawaii.hacc.specifications;

import lombok.Getter;

@Getter
public class AggregateDataSpecification implements GrantsSpecification {

  private final String aggregateField;
  private final String filter;
  private final String filterValue;

  public AggregateDataSpecification(String aggregateField, String filter, String filterValue) {
    this.aggregateField = aggregateField;
    this.filter = filter;
    this.filterValue = filterValue;
  }

  @Override
  public String toSqlClause() {
    return null;
  }

}
