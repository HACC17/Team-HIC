package gov.ehawaii.hacc.service;

import java.util.List;
import java.util.Map;
import gov.ehawaii.hacc.model.Grant;

public interface GrantsService {

  boolean insertGrant(Grant grant);

  List<Grant> getGrants(Map<String, Object> parameters);

  List<Grant> getTopFiveGrantsForFiscalYear(String year);

  List<Map<String, Object>> getTopNGrants(int top, String field1, String field2);

  List<String> getAllOrganizations();

  List<String> getAllStatuses();

  List<Map<String, Long>> getOrganizationDataOverTime(String organization, String field);

}
