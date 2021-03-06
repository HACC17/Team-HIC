package gov.ehawaii.hacc.specifications;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import gov.ehawaii.hacc.repositories.impl.Filters;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * This specification can be used to generate a query that sums together all the values in a given
 * column that satisfy zero, one, or more conditions.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
@Getter
public class TotalsSpecification implements SqlSpecification {

  private static final Logger LOGGER = LogManager.getLogger(TotalsSpecification.class);

  private final String totalsQuery;
  private final String sumColumn;
  @Getter(AccessLevel.NONE)
  private final String[] filters;
  private final Object[] filterValues;
  @Setter
  private ColumnSpecification colSpec;

  /**
   * Constructs a new {@link TotalsSpecification}.
   * 
   * @param totalsQuery A query that contains a <code>SUM</code> function.
   * @param sumColumn The column whose values are summed together.
   * @param filters An array of filters to apply to the query.
   * @param filterValues An array of values for the filters.
   */
  public TotalsSpecification(final String totalsQuery, final String sumColumn,
      final String[] filters, final Object[] filterValues) {
    this.totalsQuery = totalsQuery;
    this.sumColumn = sumColumn;
    this.filters = filters == null ? new String[0] : filters.clone();
    this.filterValues = filterValues == null ? new String[0] : filterValues.clone();
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

  @Override
  public final String toSqlClause() {
    return String.format(totalsQuery, sumColumn, buildFilter());
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
