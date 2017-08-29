package gov.ehawaii.hacc.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/search")
public class SearchController {

  @RequestMapping(method = RequestMethod.GET)
  public String showSearchPage() {
    return "search";
  }

}
