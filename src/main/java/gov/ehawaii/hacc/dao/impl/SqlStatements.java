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

  // INSERT
  public static final String INSERT_GRANT = "INSERT INTO GRANTS VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

  public static final String INSERT_INTO = "INSERT INTO %s(%s) VALUES(?)";

  // SELECT
  public static final String GET_ID = "SELECT ID FROM %s WHERE %s = ?";

  public static final String GET_GRANT_BY = "SELECT * FROM GRANTS WHERE %s = ?";

  public static final String GET_ALL_GRANTS = "SELECT * FROM GRANTS";

  public static final String COUNT = "SELECT COUNT(*) FROM %s WHERE %s = ?";

}
