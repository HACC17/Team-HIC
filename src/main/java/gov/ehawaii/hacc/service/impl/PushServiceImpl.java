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
 * This service will push all the information about each grant that is stored in a CSV file to
 * Hawaii's Open Data Portal (http://data.hawaii.gov).
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
@Service
public class PushServiceImpl implements PushService {

  private static final Logger LOGGER = LogManager.getLogger(PushServiceImpl.class);

  private static final String[] FILTERED_WORDS = { "domain", "username", "password", "appToken", "smtpUsername", "smtpPassword" };

  @Autowired
  private PropertiesFileManager propertiesFileManager;

  @Value("${scripts.path}")
  private String scriptPath;

  @Value("${scripts.names}")
  private String scriptNames;

  @Override
  public final String pushData() {
    if (scriptNames.contains(",")) {
      StringBuffer buffer = new StringBuffer();
      for (String scriptName : scriptNames.split(",")) {
        String command = "sh " + scriptPath + scriptName;
        buffer.append(executeCommand(command));
      }
      return buffer.toString();
    }
    else {
      String command = "sh " + scriptPath + scriptNames;
      return executeCommand(command);
    }
  }

  @Override
  public final void setCronEnabled(final boolean cronEnabled) {
    propertiesFileManager.saveProperty(PushService.PUSH_CRON, cronEnabled ? "yes" : "no");
  }

  /**
   * Executes the given command and then returns the output of the command.
   * 
   * @param command The command to execute.
   * @return The output of the command.
   */
  private static String executeCommand(final String command) {

    StringBuffer output = new StringBuffer();

    try {
      Process p = Runtime.getRuntime().exec(command);
      p.waitFor();
      output.append(getOutput(p.getInputStream(), "Output from " + command));
      output.append(getOutput(p.getErrorStream(), "Error output from " + command));
    }
    catch (IOException | InterruptedException e) {
      LOGGER.error("A problem was encountered while executing the command: " + e.getMessage(), e);
    }

    return output.toString();

  }

  /**
   * Gets the output from the given input stream.
   * 
   * @param is The input stream from which to get the output.
   * @param title The title for the output.
   * @return The output from the given input stream.
   */
  private static String getOutput(final InputStream is, final String title) {
    try (BufferedReader reader =
        new BufferedReader(new InputStreamReader(is, Charset.defaultCharset()))) {
      StringBuffer output = new StringBuffer();
      String line = "";
      while ((line = reader.readLine()) != null) {
        boolean appendLine = true;
        for (String word : FILTERED_WORDS) {
          if (line.contains(word)) {
            appendLine = false;
            break;
          }
        }
        if (appendLine) {
          output.append(line + "\n");
        }
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
