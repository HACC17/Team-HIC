package gov.ehawaii.hacc.specifications;

/**
 * Implementations of this interface contain criteria used to filter results from an SQL query
 * (MySQL, Oracle, SQL Server, etc.).
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
public interface SqlSpecification extends Specification {

  /**
   * The name of the table used in the query.
   * 
   * @return The name of the table.
   */
  String getTable();

  /**
   * The name of the column used in the query.
   * 
   * @return The name of the column.
   */
  String getColumn();

  /**
   * The value used to filter results from a SQL query.
   * 
   * @return The value.
   */
  Object getValue();

  /**
   * The query that will be used to get results from a SQL database.
   * 
   * @return The SQL query.
   */
  String toSqlClause();

}
