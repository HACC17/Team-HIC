package gov.ehawaii.hacc.repositories.impl;

/**
 * This class contains SQL statements that are used to query an SQL database or insert data into the database.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
public final class SqlStatements {

  public static final String INSERT_GRANT = "INSERT INTO GRANTS VALUES(DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

  public static final String INSERT_INTO = "INSERT INTO %s (%s) VALUES (?)";

  public static final String GET_ID = "SELECT ID FROM %s WHERE %s = '%s'";

  public static final String GET_VALUE = "SELECT %s FROM %s WHERE ID = ?";

  public static final String GET_ALL_GRANTS = "SELECT * FROM %s";

  public static final String COUNT = "SELECT COUNT(*) FROM %s ";

  public static final String GET_AGGREGATE_DATA_FOR_ORGANIZATION = "SELECT FISCAL_YEAR, SUM(%s) FROM GRANTS WHERE ORGANIZATION_ID = ?";

  public static final String GET_TOP_N_DATA = "SELECT %s, SUM(%s) FROM GRANTS";

  public static final String GET_TOTAL_FOR_EACH_LOCATION = "SELECT L.LOCATION, SUM(G.%s) FROM GRANTS G, LOCATIONS L "
    + "WHERE G.LOCATION_ID = L.ID AND %s GROUP BY LOCATION_ID";

  public static final String GET_ALL_DATA_FOR_LOCATION = "SELECT GRANT_TYPE, SUM(G.%s) FROM GRANTS G, GRANT_TYPES T, LOCATIONS L "
    + "WHERE G.GRANT_TYPE_ID = T.ID AND G.LOCATION_ID = L.ID AND LOCATION = ? AND %s "
    + "GROUP BY GRANT_TYPE";

  public static final String GET_TOP_N_ORGANIZATIONS_FOR_EACH_LOCATION = "SELECT O.ORGANIZATION, SUM(G.%s) FROM GRANTS G, LOCATIONS L, ORGANIZATIONS O "
    + "WHERE G.LOCATION_ID = L.ID AND G.ORGANIZATION_ID = O.ID AND %s "
    + "GROUP BY ORGANIZATION ORDER BY SUM(G.xxx) DESC LIMIT yyy";

  /** Do not instantiate this class. */
  private SqlStatements() {
  }

}
