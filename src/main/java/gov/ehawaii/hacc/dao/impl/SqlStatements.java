package gov.ehawaii.hacc.dao.impl;

public final class SqlStatements {

  private SqlStatements() {
  }

  // Table names
  public static final String GRANT_STATUSES = "GRANT_STATUSES";

  public static final String GRANT_TYPES = "GRANT_TYPES";

  public static final String ORGANIZATIONS = "ORGANIZATIONS";

  public static final String PROJECTS = "PROJECTS";

  public static final String STRATEGIC_PRIORITIES = "STRATEGIC_PRIORITIES";

  public static final String STRATEGIC_RESULTS = "STRATEGIC_RESULTS";

  // Column names
  public static final String STATUS = "STATUS";

  public static final String ORGANIZATION = "ORGANIZATION";

  public static final String GRANT_STATUS_ID = "GRANT_STATUS_ID";

  public static final String ORGANIZATION_ID = "ORGANIZATION_ID";

  // INSERT
  public static final String INSERT_GRANT = "INSERT INTO GRANTS VALUES(DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

  public static final String INSERT_INTO = "INSERT INTO %s(%s) VALUES(?)";

  // SELECT
  public static final String GET_ID = "SELECT ID FROM %s WHERE %s = ?";

  public static final String GET_VALUE = "SELECT %s FROM %s WHERE ID = ?";

  public static final String GET_GRANT_BY = "SELECT * FROM GRANTS WHERE %s = ?";

  public static final String GET_ALL_GRANTS = "SELECT * FROM GRANTS";

  public static final String COUNT = "SELECT COUNT(*) FROM %s WHERE %s = ?";

  public static final String FISCAL_YEAR = "SELECT ORGANIZATION_ID, SUM(AMOUNT) FROM GRANTS WHERE FISCAL_YEAR = %d GROUP BY ORGANIZATION_ID, AMOUNT ORDER BY AMOUNT DESC LIMIT 5";

  public static final String TOP_N = "SELECT %s_ID, SUM(%s) FROM GRANTS GROUP BY %s_ID, %s ORDER BY %s DESC LIMIT %d";

  public static final String GET_ALL_ORGANIZATIONS = "SELECT ORGANIZATION FROM ORGANIZATIONS ORDER BY ORGANIZATION ASC";

  public static final String GET_ALL_STATUSES = "SELECT STATUS FROM GRANT_STATUSES ORDER BY STATUS ASC";

  public static final String GET_DATA_FOR_ORG = "SELECT SUM(%s), FISCAL_YEAR FROM GRANTS WHERE ORGANIZATION_ID = %d GROUP BY FISCAL_YEAR ORDER BY FISCAL_YEAR ASC";

  public static final String AMOUNT_GTE = "AMOUNT >= ?";

}
