package gov.ehawaii.hacc.specifications;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TopNFiscalYearSpecification extends TopNSpecification {

  private static final Logger LOGGER = LogManager.getLogger(TopNFiscalYearSpecification.class);

  private final int fiscalYear;

  public TopNFiscalYearSpecification(int n, String column1, String column2, int fiscalYear) {
    super(n, column1, column2);
    this.fiscalYear = fiscalYear;
  }

  @Override
  public String toSqlClause() {
    String stmt = String.format("%s WHERE FISCAL_YEAR = %d %s", getSelect(), fiscalYear, getFilter());
    LOGGER.info("SQL Statement: " + stmt);
    return stmt;
  }

}
