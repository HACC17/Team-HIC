package gov.ehawaii.hacc.web.controllers;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import gov.ehawaii.hacc.model.Grant;
import gov.ehawaii.hacc.service.GrantsService;

@Controller
public class MainController {

  private final List<Grant> grants = new ArrayList<>();
  private final List<String> organizations = new ArrayList<>();

  @Autowired
  private GrantsService grantsService;

  @PostConstruct
  public void init() {
    grants.addAll(grantsService.getAllData());
    organizations.addAll(grantsService.getAllOrganizations());
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String showIndexPage(Model model) {
    model.addAttribute("all", grants);
    model.addAttribute("organizations", organizations);
    return "index";
  }

  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public String showLoginPage() {
    return "login";
  }

}
