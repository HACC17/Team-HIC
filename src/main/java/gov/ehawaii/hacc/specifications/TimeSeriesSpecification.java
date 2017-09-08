package gov.ehawaii.hacc.specifications;

import lombok.Getter;

@Getter
public class TimeSeriesSpecification implements SqlSpecification {

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
  public String getTable() {
    return table;
  }

  @Override
  public String getColumn() {
    return column;
  }

  @Override
  public Object getValue() {
    return value;
  }

  @Override
  public String toSqlClause() {
    return String.format(timeSeriesQuery + " GROUP BY FISCAL_YEAR ORDER BY FISCAL_YEAR ASC", aggregateField);
  }

}
