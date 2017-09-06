package gov.ehawaii.hacc.specifications;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import gov.ehawaii.hacc.repositories.impl.SqlStatements;

public class TopNFiscalYearSpecification extends TopNSpecification {

  private static final Logger LOGGER = LogManager.getLogger(TopNFiscalYearSpecification.class);

  private final int fiscalYear;

  public TopNFiscalYearSpecification(int n, String table, String column1, String column2, int fiscalYear) {
    super(n, table, column1, column2);
    this.fiscalYear = fiscalYear;
  }

  @Override
  public String toSqlClause() {
    String stmt = String.format(
        SqlStatements.GET_TOP_N_DATA + " WHERE FISCAL_YEAR = %d GROUP BY %s, %s ORDER BY %s DESC LIMIT %d",
        getColumn1(), getColumn2(), fiscalYear, getColumn1(), getColumn2(), getColumn2(), getN());
    LOGGER.info("SQL Statement: " + stmt);
    return stmt;
  }

}
