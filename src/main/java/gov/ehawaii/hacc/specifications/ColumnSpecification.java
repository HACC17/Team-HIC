package gov.ehawaii.hacc.specifications;

import lombok.Getter;

/**
 * This specification is used to retrieve values from a given column.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
@Getter
public class ColumnSpecification implements SqlSpecification {

  private final String table;
  private final String column;
  private final boolean distinct;

  /**
   * Creates a new {@link ColumnSpecification}.
   * 
   * @param table The table that contains the given column.
   * @param column The column from which to retrieve all values.
   */
  public ColumnSpecification(String table, String column) {
    this(table, column, false);
  }

  /**
   * Creates a new {@link ColumnSpecification}.
   * 
   * @param table The table that contains the given column.
   * @param column The column from which to retrieve values.
   * @param distinct <code>true</code> to retrieve only distinct values, <code>false</code> to retrieve all values.
   */
  public ColumnSpecification(String table, String column, boolean distinct) {
    this.table = table;
    this.column = column;
    this.distinct = distinct;
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
    throw new UnsupportedOperationException("This method is not supported.");
  }

  @Override
  public String toSqlClause() {
    if (distinct) {
      return String.format("SELECT DISTINCT %s FROM %s ORDER BY %s ASC", column, table, column);
    }
    return String.format("SELECT %s FROM %s ORDER BY %s ASC", column, table, column);
  }

}
