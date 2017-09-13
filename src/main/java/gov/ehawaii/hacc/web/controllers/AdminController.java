package gov.ehawaii.hacc.web.controllers;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import gov.ehawaii.hacc.importers.CsvImporter;
import gov.ehawaii.hacc.importers.ExcelImporter;
import gov.ehawaii.hacc.model.Grant;
import gov.ehawaii.hacc.service.GrantsService;

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
  private CsvImporter csvImporter;

  @Autowired
  private ExcelImporter excelImporter;

  /**
   * Displays the main page for an admin user. The page contains a form that the admin submits in
   * order to add a grant to the database.
   * 
   * @param model The model to which to add data for the form.
   * @return The name of the JSP file that contains the form.
   */
  @RequestMapping(method = RequestMethod.GET)
  public final String showAdminPage(final Model model) {
    model.addAttribute("grant", new Grant());
    model.addAttribute("organizations", grantsService.getAllOrganizations());
    model.addAttribute("statuses", grantsService.getAllGrantStatuses());
    model.addAttribute("priorities", grantsService.getAllStrategicPriorities());
    model.addAttribute("results", grantsService.getAllStrategicResults());
    model.addAttribute("types", grantsService.getAllGrantTypes());
    model.addAttribute("locations", grantsService.getAllLocations());
    return "admin/index";
  }

  /**
   * When a <code>GET</code> request is sent to the <code>/import</code> endpoint, the application
   * will import sample data from a file. The type of the file depends on the value of the parameter
   * that is sent with the request. Two file types are supported:
   * <ul>
   * <li><code>csv</code>: A CSV file</li>
   * <li><code>excel</code>: A Microsoft Excel file</li>
   * </ul>
   * 
   * @param ext The type of the file from which to get data to import.
   * @return The admin user will be redirected to the <code>/admin</code> endpoint.
   */
  @RequestMapping(value = "/import", method = RequestMethod.GET)
  public final String importSampleData(@RequestParam("ext") final String ext) {
    if (ext == null || ext.isEmpty()) {
      LOGGER.error("ext parameter is null or empty.");
      return "redirect:/admin";
    }

    switch (ext) {
    case "csv":
      if (csvImporter.importData()) {
        LOGGER.info("Successfully imported sample data from CSV file.");
      }
      break;
    case "excel":
      if (excelImporter.importData()) {
        LOGGER.info("Successfully imported sample data from Excel file.");
      }
      break;
    default:
      LOGGER.error(ext + " not supported, yet.");
    }
    return "redirect:/admin";
  }

  /**
   * A <code>POST</code> request is sent to the <code>/admin</code> endpoint to add a grant to the
   * database. Information about the grant is sent along with the request.
   * 
   * @param grant The grant to add to the database.
   * @return The admin user will be redirected to the <code>/admin</code> endpoint.
   */
  @RequestMapping(method = RequestMethod.POST)
  public final String addGrant(@ModelAttribute("grant") final Grant grant) {
    if (grantsService.saveGrant(grant)) {
      LOGGER.info("Grant [" + grant + "] saved successfully.");
    }
    else {
      LOGGER.error("Grant [" + grant + "] was not saved successfully.");
    }
    return "redirect:/";
  }

  @RequestMapping(value = "/opendata", method = RequestMethod.GET)
  public final void pushToOpenData(HttpServletResponse response) throws IOException {
    response.getWriter().write("Test");
  }

}
