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
@RequestMapping("/charts")
public class ChartsController {

  @Autowired
  private GrantsService grantsService;

  @RequestMapping(value = "/fiscalYear", method = RequestMethod.GET)
  public void getDataForFiscalYear(@RequestParam("year") String searchString,
      HttpServletResponse response) throws IOException {
    response.getWriter().write(
        new ObjectMapper().writeValueAsString(grantsService.getGrantsForFiscalYear(searchString)));
  }

  @RequestMapping(value = "/top", method = RequestMethod.GET)
  public void getTopData(@RequestParam("top") String top, @RequestParam("field") String field,
      @RequestParam("criterion") String criterion, HttpServletResponse response)
      throws IOException {
    int n = Integer.parseInt(top);
    response.getWriter().write(
        new ObjectMapper().writeValueAsString(grantsService.getTopNData(n, field, criterion)));
  }

  @RequestMapping(value = "/time", method = RequestMethod.GET)
  public void getOrganizationDataOverTime(@RequestParam("org") String organization,
      @RequestParam("criterion") String criterion, HttpServletResponse response)
      throws IOException {
    response.getWriter().write(new ObjectMapper()
        .writeValueAsString(grantsService.getOrganizationDataOverTime(organization, criterion)));
  }

}
