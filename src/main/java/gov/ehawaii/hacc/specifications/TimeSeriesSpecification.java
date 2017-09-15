package gov.ehawaii.hacc.specifications;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import gov.ehawaii.hacc.repositories.impl.Filters;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

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

  private static final Logger LOGGER = LogManager.getLogger(TimeSeriesSpecification.class);

  private final String table;
  private final String column;
  private final int top;

  private final String aggregateQuery;
  private final String timeSeriesQuery;
  private final String aggregateField;

  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private final String[] filters;
  @Setter(AccessLevel.NONE)
  private final Object[] filterValues;

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
   * @param filters The filters to apply to the query.
   * @param filterValues The values for the filters.
   */
  @SuppressWarnings("checkstyle:parameternumber")
  public TimeSeriesSpecification(final String table, final String column, final int top,
      final String aggregateQuery, final String timeSeriesQuery, final String aggregateField,
      final String[] filters, final Object[] filterValues) {
    this.table = table;
    this.column = column;
    this.top = top;
    this.aggregateQuery = aggregateQuery;
    this.timeSeriesQuery = timeSeriesQuery;
    this.aggregateField = aggregateField;
    this.filters = filters == null ? new String[0] : filters.clone();
    this.filterValues = filterValues == null ? new Object[0] : filterValues.clone();
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
    return String.format(aggregateQuery, aggregateField, buildFilter(), aggregateField);
  }

  /**
   * Returns the time series query.
   * 
   * @return The time series query.
   */
  public final String getTimeSeriesQuery() {
    return String.format(timeSeriesQuery, aggregateField, buildFilter());
  }

  @Override
  public final String toSqlClause() {
    return String.format(timeSeriesQuery + " GROUP BY FISCAL_YEAR ORDER BY FISCAL_YEAR ASC",
        aggregateField);
  }

  /**
   * Returns a copy of the array of values for the filters.
   * 
   * @return A copy of the array of values for the filters.
   */
  public final Object[] getFilterValues() {
    return filterValues.clone();
  }

  /**
   * Creates a string that contains each filter in the array.
   * 
   * @return A string that can be used in a <code>WHERE</code> clause.
   */
  private String buildFilter() {
    StringBuffer buffer = new StringBuffer();
    for (String filter : filters) {
      buffer.append(Filters.FILTERS_MAP.get(filter));
      buffer.append("AND ");
    }
    String filter = buffer.toString().trim();
    int idx = filter.lastIndexOf(" AND");
    filter = filter.substring(0, idx < 0 ? filter.length() : idx);
    LOGGER.debug("Filter: " + filter);
    return filter;
  }

}
