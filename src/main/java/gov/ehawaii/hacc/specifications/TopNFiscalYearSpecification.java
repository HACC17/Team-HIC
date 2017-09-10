package gov.ehawaii.hacc.specifications;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This specification is used to retrieve the top N data for a given fiscal year.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
public class TopNFiscalYearSpecification extends TopNSpecification {

  private static final Logger LOGGER = LogManager.getLogger(TopNFiscalYearSpecification.class);

  private final int fiscalYear;

  /**
   * Creates a new {@link TopNFiscalYearSpecification}.
   * 
   * @param n N, a number greater than 0.
   * @param groupByColumn The column that will be specified in the <code>GROUP BY</code> clause.
   * @param sumColumn The column whose values are summed together.
   * @param fiscalYear The fiscal year.
   */
  public TopNFiscalYearSpecification(final int n, final String groupByColumn,
      final String sumColumn, final int fiscalYear) {
    super(n, groupByColumn, sumColumn);
    this.fiscalYear = fiscalYear;
  }

  @Override
  public final String toSqlClause() {
    String stmt =
        String.format("%s WHERE FISCAL_YEAR = %d %s", getSelect(), fiscalYear, getFilter());
    LOGGER.info("SQL Statement: " + stmt);
    return stmt;
  }

}
