package gov.ehawaii.hacc.web.controllers;

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

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String showIndexPage(Model model) {
    model.addAttribute("all", grantsService.getAllData());
    return "index";
  }

  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public String showLoginPage() {
    return "login";
  }

}
