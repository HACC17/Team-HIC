package gov.ehawaii.hacc.specifications;

import gov.ehawaii.hacc.repositories.impl.SqlStatements;
import lombok.Getter;

@Getter
public class IdSpecification implements SqlSpecification {

  public final String table;
  public final String column;
  public final Object value;

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

  @Override
  public String toSqlClause() {
    return String.format(SqlStatements.GET_ID, table, column, value);
  }

}
