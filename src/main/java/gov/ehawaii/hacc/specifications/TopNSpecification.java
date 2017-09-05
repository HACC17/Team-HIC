package gov.ehawaii.hacc.specifications;

import lombok.Getter;

@Getter
public class TopNSpecification implements GrantsSpecification {

  private final int n;
  private final String table;
  private final String column1;
  private final String column2;

  public TopNSpecification(int n, String table, String column1, String column2) {
    this.n = n;
    this.table = table;
    this.column1 = column1;
    this.column2 = column2;
  }

  @Override
  public String toSqlClause() {
    return String.format("GROUP BY %s, %s ORDER BY %s DESC LIMIT %d", column1, column2, column2, n);
  }

}
