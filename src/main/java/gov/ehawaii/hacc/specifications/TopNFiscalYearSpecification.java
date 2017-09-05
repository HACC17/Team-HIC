package gov.ehawaii.hacc.specifications;

public class TopNFiscalYearSpecification extends TopNSpecification {

  private final int fiscalYear;

  public TopNFiscalYearSpecification(int n, String table, String column1, String column2, int fiscalYear) {
    super(n, table, column1, column2);
    this.fiscalYear = fiscalYear;
  }

  @Override
  public String toSqlClause() {
    return String.format("WHERE FISCAL_YEAR = %d %s", fiscalYear, super.toSqlClause());
  }

}
