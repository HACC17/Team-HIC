package gov.ehawaii.hacc.service;

import java.util.List;
import java.util.Map;
import gov.ehawaii.hacc.model.Grant;

public interface GrantsService {

  boolean insertGrant(Grant grant);

  List<Grant> getAllGrants();

  List<Grant> getGrantsForFiscalYear(String year);

  List<Map<String, Object>> getTopNData(int top, String field, String criterion);

  List<String> getAllOrganizations();

  List<Map<String, Long>> getOrganizationDataOverTime(String organization, String criterion);

}
