package gov.ehawaii.hacc.dao;

import java.util.List;
import java.util.Map;
import gov.ehawaii.hacc.model.Grant;

public interface GrantsDao {

  boolean saveGrant(Grant grant);

  List<Grant> retrieveAll();

  List<Grant> findGrantsByFiscalYear(int fiscalYear);

  List<Map<String, Object>> retrieveTop(int top, String field, String criterion);

  String getGrantStatusForId(int grantStatusId);

}
