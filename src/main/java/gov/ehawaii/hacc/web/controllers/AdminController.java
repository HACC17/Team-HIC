package gov.ehawaii.hacc.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin")
public class AdminController {

  @RequestMapping(method = RequestMethod.GET)
  public String showAdminPage() {
    return "admin/index";
  }

}
