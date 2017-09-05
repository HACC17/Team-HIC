package gov.ehawaii.hacc.specifications;

import lombok.Getter;

@Getter
public class OrganizationSpecification implements GrantsSpecification {

  private final String organization;
  private final String column;

  public OrganizationSpecification(String organization, String column) {
    this.organization = organization;
    this.column = column;
  }

  @Override
  public String toSqlClause() {
    return null;
  }

}
