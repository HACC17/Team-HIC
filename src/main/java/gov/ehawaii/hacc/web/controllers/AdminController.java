package gov.ehawaii.hacc.web.controllers;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.ehawaii.hacc.importers.CsvImporter;
import gov.ehawaii.hacc.importers.ExcelImporter;
import gov.ehawaii.hacc.model.Grant;
import gov.ehawaii.hacc.service.GrantsService;
import gov.ehawaii.hacc.service.PushService;

/**
 * This controller handles all requests going to the <code>/admin</code> endpoint.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

  private static final Logger LOGGER = LogManager.getLogger(AdminController.class);

  @Autowired
  private GrantsService grantsService;

  @Autowired
  private PushService pushService;

  @Autowired
  private CsvImporter csvImporter;

  @Autowired
  private ExcelImporter excelImporter;


  /**
   * When a <code>GET</code> request is sent to the <code>/admin/import</code> endpoint, the application will import
   * sample data from a file. The type of the file depends on the value of the parameter that is sent with the request.
   * Two file types are supported:
   * <ul>
   * <li><code>csv</code>: A CSV file</li>
   * <li><code>excel</code>: A Microsoft Excel file</li>
   * </ul>
   * 
   * @param type The type of the file from which to get data to import.
   * @param response The server response.
   * @throws IOException If there are problems trying to send the response back to the client.
   */
  @RequestMapping(value = "/import", method = RequestMethod.GET)
  public final void importSampleData(@RequestParam("type") final String type, final HttpServletResponse response)
      throws IOException {
    Assert.notNull(type, "type must not be null");

    String msg;
    switch (type) {
    case "csv":
      if (csvImporter.importData()) {
        msg = "Successfully imported sample data from a CSV file.";
        LOGGER.info(msg);
        response.setStatus(HttpServletResponse.SC_OK);
      }
      else {
        msg = "There was a problem trying to import sample data from a CSV file.";
        LOGGER.error(msg);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      }
      break;
    case "excel":
      if (excelImporter.importData()) {
        msg = "Successfully imported sample data from a Microsoft Excel file.";
        LOGGER.info(msg);
        response.setStatus(HttpServletResponse.SC_OK);
      }
      else {
        msg = "There was a problem trying to import sample data from a Microsoft Excel file.";
        LOGGER.error(msg);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      }
      break;
    default:
      msg = type + " is not yet supported.";
      LOGGER.error(type + " is not yet supported.");
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
    response.getWriter().write(msg);
  }


  /**
   * A <code>POST</code> request is sent to the <code>/admin</code> endpoint to add a grant to the database. Information
   * about the grant is sent along with the request.
   * 
   * @param grant The grant to add to the database.
   * @param response The server response.
   */
  @RequestMapping(method = RequestMethod.POST)
  public final void addGrant(@ModelAttribute("grant") final Grant grant, final HttpServletResponse response) {
    if (grantsService.saveGrant(grant)) {
      LOGGER.info("Grant [" + grant + "] saved successfully.");
      response.setStatus(HttpServletResponse.SC_OK);
    }
    else {
      LOGGER.error("Grant [" + grant + "] was not saved successfully.");
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }


  /**
   * A <code>POST</code> request is sent to the <code>/admin/org</code> endpoint to add an
   * organization to the database.
   * 
   * @param json Contains the name of the organization to add.
   * @param response The server response.
   * @throws IOException If there are problems adding the organization to the database.
   */
  @RequestMapping(value = "/org", method = RequestMethod.POST)
  public final void addOrganization(@RequestBody final String json,
      final HttpServletResponse response) throws IOException {
    Map<String, String> parameters = new ObjectMapper().readValue(URLDecoder.decode(json, "UTF-8"),
        new TypeReference<Map<String, String>>() {
        });
    String organization = parameters.get("organization");
    if (grantsService.addNewOrganization(organization)) {
      response.setStatus(HttpServletResponse.SC_OK);
    }
    else {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }


  /**
   * A <code>GET</code> request is sent to the <code>/admin/opendata</code> endpoint to push all the
   * data stored in the repository to Hawaii's Open Data Portal.
   * 
   * @param cronEnabled "yes" to enable the Cron job, "no" to disable it.
   * @param response The server response.
   * @throws IOException If there are problems trying to send the response back to the client.
   */
  @RequestMapping(value = "/opendata", method = RequestMethod.GET)
  public final void pushToOpenData(@RequestParam("cron") final String cronEnabled,
      final HttpServletResponse response) throws IOException {
    pushService.setCronEnabled("yes".equals(cronEnabled));
    response.setContentType("text/html;charset=UTF-8");
    response.getWriter().write(pushService.pushData());
  }

}
