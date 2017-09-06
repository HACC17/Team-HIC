package gov.ehawaii.hacc.specifications;

public interface SqlSpecification extends Specification {

  String getTable();

  String getColumn();

  Object getValue();

  String toSqlClause();

}
