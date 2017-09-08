package gov.ehawaii.hacc.specifications;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import gov.ehawaii.hacc.repositories.impl.SqlStatements;
import lombok.Getter;

@Getter
public class TopNSpecification implements SqlSpecification {

  private static final Logger LOGGER = LogManager.getLogger(TopNSpecification.class);

  private final int n;
  private final String column1;
  private final String column2;

  public TopNSpecification(int n, String column1, String column2) {
    this.n = n;
    this.column1 = column1;
    this.column2 = column2;
  }

  @Override
  public String getTable() {
    throw new UnsupportedOperationException("This method is not supported.");
  }

  @Override
  public String getColumn() {
    throw new UnsupportedOperationException("This method is not supported.");
  }

  @Override
  public Object getValue() {
    throw new UnsupportedOperationException("This method is not supported.");
  }

  public String getSelect() {
    return String.format(SqlStatements.GET_TOP_N_DATA, column1, column2);
  }

  public String getFilter() {
    return String.format(" GROUP BY %s, %s ORDER BY %s DESC LIMIT %d", column1, column2, column2, n);
  }

  @Override
  public String toSqlClause() {
    String stmt = String.format("%s %s", getSelect(), getFilter());
    LOGGER.info("SQL Statement: " + stmt);
    return stmt;
  }

}
