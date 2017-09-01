package gov.ehawaii.hacc.dao;

import java.util.List;
import gov.ehawaii.hacc.model.Grant;

public interface GrantsDao {

  boolean saveGrant(Grant grant);

  List<Grant> retrieveAll();

  List<Grant> findGrantsByFiscalYear(int fiscalYear);

  List<Grant> retrieveTop(int top, String field, String criterion);

  String getGrantStatusForId(int grantStatusId);

}
