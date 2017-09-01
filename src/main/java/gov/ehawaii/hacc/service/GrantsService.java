package gov.ehawaii.hacc.service;

import java.util.List;
import gov.ehawaii.hacc.model.Grant;

public interface GrantsService {

  boolean insertGrant(Grant grant);

  List<Grant> getAllData();

  List<Grant> getDataForFiscalYear(String year);

  List<Grant> getTopData(int top, String field, String criterion);

}
