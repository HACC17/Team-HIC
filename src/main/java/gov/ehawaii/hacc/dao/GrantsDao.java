package gov.ehawaii.hacc.dao;

import java.util.List;
import gov.ehawaii.hacc.model.Grant;

public interface GrantsDao {

  boolean saveGrant(Grant grant);

  List<Grant> retrieveAll();

  List<Grant> findGrantsByFiscalYear(int fiscalYear);

  List<Grant> findGrantsByGrantType(String grantType);

  String getGrantStatusForId(int grantStatusId);

}
