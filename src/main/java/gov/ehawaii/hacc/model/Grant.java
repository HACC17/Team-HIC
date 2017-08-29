package gov.ehawaii.hacc.model;

import java.io.Serializable;
import lombok.Data;

@Data
public class Grant implements Serializable {

  private static final long serialVersionUID = 1L;

  private int fiscalYear;
  private String grantType;
  private String organization;
  private String project;
  private int amount;
  private String location;
  private String strategicPriority;
  private String strategicResults;
  private int totalNumberServed;
  private int numberNativeHawaiiansServed;
  private int grantStatusId;

}
