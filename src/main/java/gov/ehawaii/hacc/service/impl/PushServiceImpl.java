package gov.ehawaii.hacc.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
      BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

      String line = "";
      while ((line = reader.readLine()) != null) {
        output.append(line + "\n");
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    return output.toString();

  }

}
