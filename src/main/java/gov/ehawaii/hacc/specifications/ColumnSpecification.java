package gov.ehawaii.hacc.specifications;

import lombok.Getter;

@Getter
public class ColumnSpecification implements GrantsSpecification {

  private final String table;
  private final String column;
  private final boolean distinct;

  public ColumnSpecification(String table, String column) {
    this.table = table;
    this.column = column;
    this.distinct = false;
  }

  public ColumnSpecification(String table, String column, boolean distinct) {
    this.table = table;
    this.column = column;
    this.distinct = distinct;
  }

  @Override
  public String toSqlClause() {
    if (distinct) {
      return String.format("SELECT DISTINCT %s FROM %s ORDER BY %s ASC", column, table, column);
    }
    return String.format("SELECT %s FROM %s ORDER BY %s ASC", column, table, column);
  }

}
