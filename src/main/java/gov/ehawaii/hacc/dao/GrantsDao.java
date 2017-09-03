package gov.ehawaii.hacc.dao;

import java.util.List;
import java.util.Map;
import gov.ehawaii.hacc.model.Grant;

public interface GrantsDao {

  boolean saveGrant(Grant grant);

  List<Grant> getGrants(String whereClause, Object[] arguments);

  List<Grant> findTopFiveGrantsForFiscalYear(int fiscalYear);

  List<Map<String, Object>> getTopNGrants(int top, String field1, String field2);

  String getGrantStatusForId(int grantStatusId);

  List<String> getAllOrganizations();

  List<Map<String, Long>> getOrganizationDataOverTime(String organization, String field);

}
