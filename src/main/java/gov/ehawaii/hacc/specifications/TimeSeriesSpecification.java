package gov.ehawaii.hacc.specifications;

import lombok.Getter;

/**
 * This specification is used in queries that sum data over a period of time. For example, if you
 * want to sum the values of <code>AMOUNT</code>, you should pass in the following query for
 * <code>timeSeriesQuery</code>:<br />
 * <br />
 * 
 * <code>&nbsp;&nbsp;SELECT FISCAL_YEAR, SUM(%s) FROM GRANTS</code><br />
 * <br />
 * 
 * and <code>AMOUNT</code> for <code>aggregateField</code>.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
@Getter
public class TimeSeriesSpecification implements SqlSpecification {

  private final String table;
  private final String column;
  private final int top;

  private final String aggregateQuery;
  private final String timeSeriesQuery;
  private final String aggregateField;

  /**
   * Creates a new {@link TimeSeriesSpecification}.
   * 
   * @param table The name of the table that can be used in a filter for the query produced by this
   * specification.
   * @param column The name of the column in the given table.
   * @param top N, a number greater than 0, the number of data to retrieve.
   * @param aggregateQuery The query used to aggregate the data to determine the top N.
   * @param timeSeriesQuery The query used to get information about the data over a period of time.
   * @param aggregateField The field used to aggregate data for the time series query.
   */
  public TimeSeriesSpecification(final String table, final String column, final int top,
      final String aggregateQuery, final String timeSeriesQuery, final String aggregateField) {
    this.table = table;
    this.column = column;
    this.top = top;
    this.aggregateQuery = aggregateQuery;
    this.timeSeriesQuery = timeSeriesQuery;
    this.aggregateField = aggregateField;
  }

  @Override
  public final String getTable() {
    return table;
  }

  @Override
  public final String getColumn() {
    return column;
  }

  @Override
  public final Object getValue() {
    throw new UnsupportedOperationException("This method is not supported.");
  }

  /**
   * Returns the aggregate query.
   * 
   * @return The aggregate query.
   */
  public final String getAggregateQuery() {
    return String.format(aggregateQuery, aggregateField, aggregateField);
  }

  /**
   * Returns the time series query.
   * 
   * @return The time series query.
   */
  public final String getTimeSeriesQuery() {
    return String.format(timeSeriesQuery, aggregateField);
  }

  @Override
  public final String toSqlClause() {
    return String.format(timeSeriesQuery + " GROUP BY FISCAL_YEAR ORDER BY FISCAL_YEAR ASC",
        aggregateField);
  }

}
