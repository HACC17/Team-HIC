package gov.ehawaii.hacc.model;

import java.io.Serializable;
import lombok.Data;

/**
 * This class contains information about one grant, such as its status and the total number of people served by it.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
@Data
public class Grant implements Serializable {

  private static final long serialVersionUID = 1L;

  private long id;
  private int fiscalYear;
  private String grantType;
  private String organization;
  private String project;
  private long amount;
  private String location;
  private String strategicPriority;
  private String strategicResults;
  private int totalNumberServed;
  private int numberNativeHawaiiansServed;
  private String grantStatus;


  @Override
  public String toString() {
    return "{" + "fiscalYear=" + fiscalYear + ", grantType=" + grantType + ", organization=" + organization
        + ", project=" + project + ", amount=" + amount + ", location=" + location + ", strategicPriority="
        + strategicPriority + ", strategicResults=" + strategicResults + ", totalNumberServed=" + totalNumberServed
        + ", numberNativeHawaiiansServed=" + numberNativeHawaiiansServed + ", grantStatus=" + grantStatus + "}";
  }

}
