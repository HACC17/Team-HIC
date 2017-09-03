package gov.ehawaii.hacc.web.controllers;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import gov.ehawaii.hacc.service.GrantsService;

@Controller
public class MainController {

  @Autowired
  private GrantsService grantsService;

  @PostConstruct
  public void init() {
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String showIndexPage(Model model) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("FISCAL_YEAR", "2016");
    model.addAttribute("all", grantsService.getGrants(parameters));
    return "index";
  }

  @RequestMapping(value = "/charts", method = RequestMethod.GET)
  public String showChartsPage(Model model) {
    model.addAttribute("organizations", grantsService.getAllOrganizations());
    return "charts";
  }

  @RequestMapping(value = "/reports", method = RequestMethod.GET)
  public String showReportsPage(Model model) {
    return "reports";
  }

  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public String showLoginPage() {
    return "login";
  }

}
