package gov.ehawaii.hacc.web.controllers;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
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

@Controller
@RequestMapping("/admin")
public class AdminController {

  private static final Logger LOGGER = LogManager.getLogger(AdminController.class);

  private final List<Grant> grants = new ArrayList<>();

  @Autowired
  private GrantsService grantsService;

  @Autowired
  private CsvImporter csvImporter;

  @Autowired
  private ExcelImporter excelImporter;


  @PostConstruct
  public void init() {
    grants.addAll(grantsService.getAllData());
  }


  @RequestMapping(method = RequestMethod.GET)
  public String showAdminPage(Model model) {
    model.addAttribute("grant", new Grant());
    model.addAttribute("all", grants);
    return "admin/index";
  }


  @RequestMapping(value = "/import", method = RequestMethod.GET)
  public String importSampleData(@RequestParam("ext") String ext) {
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


  @RequestMapping(method = RequestMethod.POST)
  public String addGrant(@ModelAttribute("grant") Grant grant) {
    if (grantsService.insertGrant(grant)) {
      LOGGER.info("Grant [" + grant + "] saved successfully.");
    }
    else {
      LOGGER.error("Grant [" + grant + "] was not saved successfully.");
    }
    return "redirect:/admin";
  }

}
