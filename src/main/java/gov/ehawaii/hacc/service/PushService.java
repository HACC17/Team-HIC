package gov.ehawaii.hacc.service;

/**
 * Implementations of this interface will push data to an online service.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
public interface PushService {

  String PUSH_CRON = "push.cron";

  /**
   * Pushes the data stored in a repository, file, etc. to an online service.
   * 
   * @return The results of the operation, e.g. output.
   */
  String pushData();

  /**
   * Enables or disables the Cron background job that will push data automatically to an online
   * service.
   * 
   * @param cronEnabled True to enable the Cron job, false to disable it.
   */
  void setCronEnabled(boolean cronEnabled);

}
