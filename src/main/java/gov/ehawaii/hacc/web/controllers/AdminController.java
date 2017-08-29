package gov.ehawaii.hacc.web.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import gov.ehawaii.hacc.model.Grant;
import gov.ehawaii.hacc.service.GrantsService;

@Controller
@RequestMapping("/admin")
public class AdminController {

  private static final Logger LOGGER = LogManager.getLogger(AdminController.class);

  @Autowired
  private GrantsService grantsService;

  @RequestMapping(method = RequestMethod.GET)
  public String showAdminPage(Model model) {
    model.addAttribute("grant", new Grant());
    model.addAttribute("all", grantsService.findAll());
    return "admin/index";
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
