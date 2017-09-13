package gov.ehawaii.hacc.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import gov.ehawaii.hacc.properties.PropertiesFileManager;
import gov.ehawaii.hacc.service.PushService;

/**
 * 
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
@Service
public class PushServiceImpl implements PushService {

  private static final Logger LOGGER = LogManager.getLogger(PushServiceImpl.class);

  @Autowired
  private PropertiesFileManager propertiesFileManager;

  @Value("${script.path}")
  private String scriptPath;

  @Value("${script.name}")
  private String scriptName;

  @Override
  public String pushData() {
    String command = "sh " + scriptPath + scriptName;
    return executeCommand(command);
  }

  @Override
  public void setCronEnabled(boolean cronEnabled) {
    propertiesFileManager.saveProperty("push.cron", cronEnabled ? "yes" : "no");
  }

  /**
   * Executes the given command and then returns the output of the command.
   * 
   * @param command The command to execute.
   * @return The output of the command.
   */
  private static String executeCommand(String command) {

    StringBuffer output = new StringBuffer();

    try {
      Process p = Runtime.getRuntime().exec(command);
      p.waitFor();
      output.append(getOutput(p.getInputStream(), "Output"));
      output.append(getOutput(p.getErrorStream(), "Errors"));
    }
    catch (IOException | InterruptedException e) {
      LOGGER.error("A problem was encountered while executing the command: " + e.getMessage(), e);
    }

    return output.toString();

  }

  private static String getOutput(InputStream is, String title) {
    try (BufferedReader reader =
        new BufferedReader(new InputStreamReader(is, Charset.defaultCharset()))) {
      StringBuffer output = new StringBuffer();
      String line = "";
      while ((line = reader.readLine()) != null) {
        output.append(line + "\n");
      }
      String out = output.toString();
      return out.isEmpty() ? "" : "===== " + title + " =====\n" + out;
    }
    catch (IOException ioe) {
      LOGGER.error("A problem was encountered while reading in the output of the command: "
          + ioe.getMessage(), ioe);
      return "";
    }
  }

}
