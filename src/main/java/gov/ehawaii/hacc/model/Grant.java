package gov.ehawaii.hacc.model;

import java.io.Serializable;
import lombok.Data;

/**
 * This class contains information about one grant, such as its status and the total number of
 * people served by it.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
@Data
public class Grant implements Serializable {

  private static final long serialVersionUID = 1L;

  /** The ID of this grant. */
  private long id;
  /** The fiscal year. */
  private int fiscalYear;
  /** This grant's type. */
  private String grantType;
  /** The organization to which this grant belongs. */
  private String organization;
  /** The organization's project. */
  private String project;
  /** The amount for this grant. */
  private long amount;
  /** The location of the project. */
  private String location;
  /** This grant's strategic priority. */
  private String strategicPriority;
  /** This grant's strategic results. */
  private String strategicResults;
  /** The total number of people served by this grant. */
  private int totalNumberServed;
  /** The number of Native Hawaiians served by this grant. */
  private int numberNativeHawaiiansServed;
  /** The status of this grant. */
  private String grantStatus;

  @Override
  public final String toString() {
    return "{" + "fiscalYear=" + fiscalYear + ", grantType=" + grantType + ", organization="
        + organization + ", project=" + project + ", amount=" + amount + ", location=" + location
        + ", strategicPriority=" + strategicPriority + ", strategicResults=" + strategicResults
        + ", totalNumberServed=" + totalNumberServed + ", numberNativeHawaiiansServed="
        + numberNativeHawaiiansServed + ", grantStatus=" + grantStatus + "}";
  }

}
