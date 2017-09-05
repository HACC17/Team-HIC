package gov.ehawaii.hacc.dao.impl;

public class TopNGrantsSpecification implements GrantsSpecification {

  private final int n;

  public TopNGrantsSpecification(int n) {
    this.n = n;
  }

  @Override
  public String toSqlClause() {
    return String.format(" LIMIT %d", n);
  }

}
