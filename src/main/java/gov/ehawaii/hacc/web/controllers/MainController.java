package gov.ehawaii.hacc.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {

  @RequestMapping(value="/", method = RequestMethod.GET)
  public String showIndexPage() {
    return "index";
  }

  @RequestMapping(value="/login", method = RequestMethod.GET)
  public String showLoginPage() {
    return "login";
  }

  @RequestMapping(value="/admin", method = RequestMethod.GET)
  public String showAdminPage() {
    return "admin/index";
  }

}
