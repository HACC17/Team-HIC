package gov.ehawaii.hacc.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/reports")
public class ReportsController {

  @RequestMapping(method = RequestMethod.GET)
  public String showReportsPage() {
    return "reports";
  }

}
