package gov.ehawaii.hacc.dao.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class contains constants representing filters that are used to filter query results.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
public class Filters {

  /** AMOUNT values that are greater than or equal to <code>value</code>. */
  public static final String AMOUNT_GTE = "AMOUNT >= ? ";

  /** AMOUNT values that are less than or equal to <code>value</code>. */
  public static final String AMOUNT_LTE = "AMOUNT <= ? ";

  /** FISCAL_YEAR values that are equal to <code>value</code>. */
  public static final String FISCAL_YEAR = "FISCAL_YEAR = ? ";

  /** FISCAL_YEAR values that are greater than or equal to <code>value</code>. */
  public static final String FISCAL_YEAR_GTE = "FISCAL_YEAR >= ? ";

  /** FISCAL_YEAR values that are less than or equal to <code>value</code>. */
  public static final String FISCAL_YEAR_LTE = "FISCAL_YEAR <= ? ";

  /** GRANT_STATUS_ID value equal to <code>value</code>. */
  public static final String GRANT_STATUS_ID_FILTER = "GRANT_STATUS_ID = ? ";

  /** GRANT_TYPE_ID value equal to <code>value</code>. */
  public static final String GRANT_TYPE_ID_FILTER = "GRANT_TYPE_ID = ? ";

  /** ORGANIZATION_ID value equal to <code>value</code>. */
  public static final String ORGANIZATION_ID_FILTER = "ORGANIZATION_ID = ? ";

  /** PROJECT_ID value equal to <code>value</code>. */
  public static final String PROJECT_ID_FILTER = "PROJECT_ID = ? ";

  /** LOCATION_ID value equal to <code>value</code>. */
  public static final String LOCATION_ID_FILTER = "LOCATION_ID = ? ";

  /** STRATEGIC_PRIORITY_ID value equal to <code>value</code>. */
  public static final String STRATEGIC_PRIORITY_ID_FILTER = "STRATEGIC_PRIORITY_ID = ? ";

  /** STRATEGIC_RESULT_ID value equal to <code>value</code>. */
  public static final String STRATEGIC_RESULTS_ID_FILTER = "STRATEGIC_RESULT_ID = ? ";

  /** NUMBER_NATIVE_HAWAIIANS_SERVED values that are greater than or equal to <code>value</code>. */
  public static final String NUMBER_NATIVE_HAWAIIANS_SERVED_GTE = "NUMBER_NATIVE_HAWAIIANS_SERVED >= ? ";

  /** NUMBER_NATIVE_HAWAIIANS_SERVED values that are less than or equal to <code>value</code>. */
  public static final String NUMBER_NATIVE_HAWAIIANS_SERVED_LTE = "NUMBER_NATIVE_HAWAIIANS_SERVED <= ? ";

  /** TOTAL_NUMBER_SERVED values that are greater than or equal to <code>value</code>. */
  public static final String TOTAL_NUMBER_SERVED_GTE = "TOTAL_NUMBER_SERVED >= ? ";

  /** TOTAL_NUMBER_SERVED values that are less than or equal to <code>value</code>. */
  public static final String TOTAL_NUMBER_SERVED_LTE = "TOTAL_NUMBER_SERVED <= ? ";

  /** This map contains filters that are used in queries. */
  public static final Map<String, String> FILTERS_MAP;
  static {
    Map<String, String> filters = new HashMap<>();
    filters.put("status", GRANT_STATUS_ID_FILTER);
    filters.put("type", GRANT_TYPE_ID_FILTER);
    filters.put("organization", ORGANIZATION_ID_FILTER);
    filters.put("project", PROJECT_ID_FILTER);
    filters.put("location", LOCATION_ID_FILTER);
    filters.put("priority", STRATEGIC_PRIORITY_ID_FILTER);
    filters.put("result", STRATEGIC_RESULTS_ID_FILTER);
    filters.put("fiscal", FISCAL_YEAR);
    filters.put("fiscal-gte", FISCAL_YEAR_GTE);
    filters.put("fiscal-lte", FISCAL_YEAR_LTE);
    filters.put("amount-gte", AMOUNT_GTE);
    filters.put("amount-lte", AMOUNT_LTE);
    filters.put("total-gte", TOTAL_NUMBER_SERVED_GTE);
    filters.put("total-lte", TOTAL_NUMBER_SERVED_LTE);
    filters.put("hawaiians-gte", NUMBER_NATIVE_HAWAIIANS_SERVED_GTE);
    filters.put("hawaiians-lte", NUMBER_NATIVE_HAWAIIANS_SERVED_LTE);
    FILTERS_MAP = Collections.unmodifiableMap(filters);
  }

  /** Do not instantiate this class. */
  private Filters() {
  }

}
