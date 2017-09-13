package gov.ehawaii.hacc.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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

  @Value("${script.path}")
  private String scriptPath;

  @Value("${script.name}")
  private String scriptName;

  @Override
  public String pushData() {
    String command = "sh " + scriptPath + scriptName;
    return executeCommand(command);
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
      try (BufferedReader reader =
          new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.defaultCharset()))) {

        String line = "";
        while ((line = reader.readLine()) != null) {
          output.append(line + "\n");
        }
      }
      try (BufferedReader reader =
          new BufferedReader(new InputStreamReader(p.getErrorStream(), Charset.defaultCharset()))) {

        String line = "";
        while ((line = reader.readLine()) != null) {
          output.append(line + "\n");
        }
      }
    }
    catch (IOException | InterruptedException e) {
      LOGGER.error("There was a problem executing the command: " + e.getMessage(), e);
    }

    return output.toString();

  }

}
