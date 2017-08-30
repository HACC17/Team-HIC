package gov.ehawaii.hacc.service;

import java.util.List;
import gov.ehawaii.hacc.model.Grant;

public interface GrantsService {

  boolean insertGrant(Grant grant);

  List<Grant> findAll();

  List<Grant> find(String searchString, String searchBy);

}
