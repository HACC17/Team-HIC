package gov.ehawaii.hacc.service;

import java.util.List;
import java.util.Map;
import gov.ehawaii.hacc.model.Grant;

public interface GrantsService {

  boolean insertGrant(Grant grant);

  List<Grant> getAllData();

  List<Grant> getDataForFiscalYear(String year);

  List<Map<String, Object>> getTopData(int top, String field, String criterion);

}
