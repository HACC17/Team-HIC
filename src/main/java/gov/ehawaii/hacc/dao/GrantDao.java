package gov.ehawaii.hacc.dao;

import java.util.List;
import gov.ehawaii.hacc.model.Grant;

public interface GrantDao {

  boolean saveGrant(Grant grant);

  List<Grant> findGrantsByFiscalYear(int fiscalYear);

  List<Grant> findGrantsByGrantType(String grantType);

}
