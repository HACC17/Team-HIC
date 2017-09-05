package gov.ehawaii.hacc.dao.impl;

public final class SqlStatements {

  private SqlStatements() {
  }

  // Column names
  public static final String AMOUNT = "AMOUNT";

  public static final String FISCAL_YEAR = "FISCAL_YEAR";

  public static final String GRANT_STATUS = "STATUS";

  public static final String GRANT_TYPE = "GRANT_TYPE";

  public static final String ORGANIZATION = "ORGANIZATION";

  public static final String ORGANIZATION_ID = "ORGANIZATION_ID";

  public static final String PROJECT = "PROJECT";

  public static final String PROJECT_ID = "PROJECT_ID";

  public static final String LOCATION = "LOCATION";

  public static final String STATUS = "STATUS";

  public static final String STRATEGIC_PRIORITY = "STRATEGIC_PRIORITY";

  public static final String STRATEGIC_RESULT = "STRATEGIC_RESULT";

  // INSERT
  public static final String INSERT_GRANT = "INSERT INTO GRANTS VALUES(DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

  public static final String INSERT_INTO = "INSERT INTO %s(%s) VALUES(?)";

  // SELECT
  public static final String GET_ID = "SELECT ID FROM %s WHERE %s = '%s'";

  public static final String GET_VALUE = "SELECT %s FROM %s WHERE ID = ?";

  public static final String GET_ALL_GRANTS = "SELECT * FROM GRANTS";

  public static final String GET_TOTAL_AMOUNT_FOR_EACH_ORG = "SELECT ORGANIZATION_ID, SUM(AMOUNT) FROM GRANTS WHERE FISCAL_YEAR = %d GROUP BY ORGANIZATION_ID, AMOUNT ORDER BY AMOUNT DESC LIMIT 5";

  public static final String COUNT = "SELECT COUNT(*) FROM %s ";

  public static final String GET_DATA_FOR_ORG_FOR_EACH_FISCAL_YEAR = "SELECT FISCAL_YEAR, SUM(%s) FROM GRANTS WHERE ORGANIZATION_ID = %d GROUP BY FISCAL_YEAR ORDER BY FISCAL_YEAR ASC";

  public static final String GET_TOP_N_DATA = "SELECT %s, SUM(%s) FROM GRANTS";

  public static final String GET_TOTAL_FOR_EACH_LOCATION = "SELECT L.LOCATION, SUM(G.%s) FROM GRANTS G, LOCATIONS L WHERE G.LOCATION_ID = L.ID AND FISCAL_YEAR = ? GROUP BY LOCATION_ID";

  public static final String GET_ALL_DATA_FOR_LOCATION = "SELECT ORGANIZATION, SUM(%s) FROM GRANTS G, ORGANIZATIONS O, LOCATIONS L WHERE G.ORGANIZATION_ID = O.ID AND G.LOCATION_ID = L.ID AND LOCATION = ? AND FISCAL_YEAR = ? GROUP BY ORGANIZATION";

}
