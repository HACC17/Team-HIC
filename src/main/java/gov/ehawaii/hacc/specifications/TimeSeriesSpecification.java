package gov.ehawaii.hacc.specifications;

import lombok.Getter;

@Getter
public class TimeSeriesSpecification implements GrantsSpecification {

  private final String table;
  private final String column;
  private final String value;

  private final String timeSeriesQuery;
  private final String aggregateField;

  public TimeSeriesSpecification(String table, String column, String value,
      String timeSeriesQuery, String aggregateField) {
    this.table = table;
    this.column = column;
    this.value = value;
    this.timeSeriesQuery = timeSeriesQuery;
    this.aggregateField = aggregateField;
  }

  @Override
  public String toSqlClause() {
    return " GROUP BY FISCAL_YEAR ORDER BY FISCAL_YEAR ASC";
  }

}
