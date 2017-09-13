package gov.ehawaii.hacc.service;

/**
 * 
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
public interface PushService {

  String PUSH_CRON = "push.cron";

  /**
   * 
   * 
   * @return
   */
  String pushData();

  /**
   * 
   * 
   * @param cronEnabled
   */
  void setCronEnabled(boolean cronEnabled);

}
