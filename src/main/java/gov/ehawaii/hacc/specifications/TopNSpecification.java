package gov.ehawaii.hacc.specifications;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import gov.ehawaii.hacc.repositories.impl.SqlStatements;
import lombok.Getter;

@Getter
public class TopNSpecification implements SqlSpecification {

  private static final Logger LOGGER = LogManager.getLogger(TopNSpecification.class);

  private final int n;
  private final String table;
  private final String column1;
  private final String column2;

  public TopNSpecification(int n, String table, String column1, String column2) {
    this.n = n;
    this.table = table;
    this.column1 = column1;
    this.column2 = column2;
  }

  @Override
  public String getTable() {
    return table;
  }

  @Override
  public String getColumn() {
    throw new UnsupportedOperationException("This method is not supported.");
  }

  @Override
  public Object getValue() {
    throw new UnsupportedOperationException("This method is not supported.");
  }

  @Override
  public String toSqlClause() {
    String stmt = String.format(SqlStatements.GET_TOP_N_DATA + " GROUP BY %s, %s ORDER BY %s DESC LIMIT %d", column1, column2,
        column1, column2, column2, n);
    LOGGER.info("SQL Statement: " + stmt);
    return stmt;
  }

}
