package gov.ehawaii.hacc.specifications;

import gov.ehawaii.hacc.dao.impl.SqlStatements;
import lombok.Getter;

@Getter
public class IdSpecification implements GrantsSpecification {

  public final String table;
  public final String column;
  public final String value;

  public IdSpecification(String table, String column, String value) {
    this.table = table;
    this.column = column;
    this.value = value;
  }

  @Override
  public String toSqlClause() {
    return String.format(SqlStatements.GET_ID, table, column, value);
  }

}
