package gov.ehawaii.hacc.specifications;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import gov.ehawaii.hacc.repositories.impl.SqlStatements;
import lombok.Getter;

/**
 * This specification is used to retrieve the top N data.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
@Getter
public class TopNSpecification implements SqlSpecification {

  private static final Logger LOGGER = LogManager.getLogger(TopNSpecification.class);

  private final int n;
  private final String groupByColumn;
  private final String sumColumn;

  /**
   * Creates a new {@link TopNSpecification}.
   * 
   * @param n N, a number greater than 0.
   * @param groupByColumn The column that will be specified in the <code>GROUP BY</code> clause.
   * @param sumColumn The column whose values are summed together.
   */
  public TopNSpecification(final int n, final String groupByColumn, final String sumColumn) {
    this.n = n;
    this.groupByColumn = groupByColumn;
    this.sumColumn = sumColumn;
  }

  @Override
  public final String getTable() {
    throw new UnsupportedOperationException("This method is not supported.");
  }

  @Override
  public final String getColumn() {
    throw new UnsupportedOperationException("This method is not supported.");
  }

  @Override
  public final Object getValue() {
    throw new UnsupportedOperationException("This method is not supported.");
  }

  /**
   * Returns the following query:<br /><br />&nbsp;&nbsp;<code>SELECT [groupByColumn], SUM([sumColumn]) FROM GRANTS</code>.
   * 
   * @return A <code>SELECT</code> statement without any other clauses, e.g. <code>WHERE</code>, <code>GROUP BY</code>, etc.
   */
  final String getSelect() {
    return String.format(SqlStatements.GET_TOP_N_DATA, groupByColumn, sumColumn);
  }

  /**
   * Returns a filter that can be used with the query returned by {@link #getSelect()}.
   * 
   * @return The following SQL clause: <code>GROUP BY [groupByColumn] ORDER BY SUM([sumColumn]) DESC LIMIT [n]</code>.
   */
  final String getFilter() {
    return String.format(" GROUP BY %s ORDER BY SUM(%s) DESC LIMIT %d", groupByColumn, sumColumn, n);
  }

  @Override
  @SuppressWarnings("checkstyle:designforextension")
  public String toSqlClause() {
    String stmt = String.format("%s %s", getSelect(), getFilter());
    LOGGER.info("SQL Statement: " + stmt);
    return stmt;
  }

}
