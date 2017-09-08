package gov.ehawaii.hacc.specifications;

import gov.ehawaii.hacc.repositories.impl.SqlStatements;
import lombok.Getter;

/**
 * This specification uses the following query to retrieve the ID for a given value.<br /><br />
 * 
 * <code>&nbsp;&nbsp;SELECT ID FROM [table] WHERE [column] = [value]</code>
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
@Getter
public class IdSpecification implements SqlSpecification {

  public final String table;
  public final String column;
  public final Object value;

  /**
   * Creates a new IdSpecification.
   * 
   * @param table The table from which to retrieve the ID.
   * @param column The column in the given table.
   * @param value The value for which to retrieve the ID.
   */
  public IdSpecification(String table, String column, Object value) {
    this.table = table;
    this.column = column;
    this.value = value;
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

  /**
   * @return The following query: <code>SELECT ID FROM [table] WHERE [column] = [value]</code>.
   */
  @Override
  public String toSqlClause() {
    return String.format(SqlStatements.GET_ID, table, column, value);
  }

}
