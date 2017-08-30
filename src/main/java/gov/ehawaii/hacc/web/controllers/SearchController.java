package gov.ehawaii.hacc.web.controllers;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.ehawaii.hacc.service.GrantsService;

@Controller
@RequestMapping("/search")
public class SearchController {

  @Autowired
  private GrantsService grantsService;

  @RequestMapping(method = RequestMethod.GET)
  public String showSearchPage() {
    return "search";
  }

  @RequestMapping(value = "/query", method = RequestMethod.GET)
  public void performSearch(@RequestParam("searchString") String searchString,
      @RequestParam("searchBy") String searchBy, HttpServletResponse response) throws IOException {
    response.getWriter().write(new ObjectMapper().writeValueAsString(grantsService.find(searchString, searchBy)));
  }

}
