package gov.ehawaii.hacc.repositories.impl;

/**
 * This class contains SQL statements that are used to query an SQL database or insert data into the
 * database.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
public final class SqlStatements {

  public static final String INSERT_GRANT =
      "INSERT INTO GRANTS VALUES(DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

  public static final String INSERT_INTO = "INSERT INTO %s (%s) VALUES (?)";

  public static final String GET_ID = "SELECT ID FROM %s WHERE %s = '%s'";

  public static final String GET_VALUE = "SELECT %s FROM %s WHERE ID = ?";

  public static final String GET_ALL_GRANTS = "SELECT * FROM %s";

  public static final String GET_AGGREGATE_DATA_FOR_ORGANIZATIONS =
      "SELECT ORGANIZATION_ID, SUM(%s) FROM GRANTS WHERE %s GROUP BY ORGANIZATION_ID ORDER BY SUM(%s) DESC LIMIT ?";

  public static final String GET_TIME_SERIES_DATA_FOR_ORGANIZATIONS =
      "SELECT FISCAL_YEAR, SUM(%s) FROM GRANTS WHERE %s AND ORGANIZATION_ID = ? GROUP BY FISCAL_YEAR ORDER BY FISCAL_YEAR ASC";

  public static final String GET_TOP_N_DATA = "SELECT %s, SUM(%s) FROM GRANTS";

  public static final String GET_TOP_N_ORGANIZATIONS_FOR_EACH_LOCATION =
      "SELECT O.ORGANIZATION, SUM(G.%s) FROM GRANTS G, LOCATIONS L, ORGANIZATIONS O "
          + "WHERE G.LOCATION_ID = L.ID AND G.ORGANIZATION_ID = O.ID AND %s "
          + "GROUP BY ORGANIZATION ORDER BY SUM(G.xxx) DESC LIMIT yyy";

  /** Do not instantiate this class. */
  private SqlStatements() {
  }

}
