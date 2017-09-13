package gov.ehawaii.hacc.cron;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import gov.ehawaii.hacc.properties.PropertiesFileManager;
import gov.ehawaii.hacc.service.PushService;

@Component("PushTask")
public class PushBackgroundTask implements Runnable {

  private static final Logger LOGGER = LogManager.getLogger(PushBackgroundTask.class);

  @Autowired
  private PushService pushService;

  @Autowired
  private PropertiesFileManager propertiesFileManager;

  @Override
  public void run() {
    if ("no".equals(propertiesFileManager.getProperty(PushService.PUSH_CRON, "no"))) {
      LOGGER.info(getClass() + " is disabled.");
      return;
    }

    LOGGER.info(getClass() + " has started.");
    LOGGER.info(pushService.pushData());
    LOGGER.info(getClass() + " has finished.");
  }

}
