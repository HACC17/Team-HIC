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
  public void getDataForFiscalYear(@RequestParam("year") String year,
      HttpServletResponse response) throws IOException {
    response.getWriter().write(new ObjectMapper()
        .writeValueAsString(grantsService.getTopFiveGrantsForFiscalYear(year)));
  }

  @RequestMapping(value = "/top", method = RequestMethod.GET)
  public void getTopData(@RequestParam("top") String top, @RequestParam("field1") String field1,
      @RequestParam("field2") String field2, HttpServletResponse response) throws IOException {
    int n = Integer.parseInt(top);
    response.getWriter().write(
        new ObjectMapper().writeValueAsString(grantsService.getTopNGrants(n, field1, field2)));
  }

  @RequestMapping(value = "/time", method = RequestMethod.GET)
  public void getOrganizationDataOverTime(@RequestParam("org") String organization,
      @RequestParam("field") String field, HttpServletResponse response)
      throws IOException {
    response.getWriter().write(new ObjectMapper()
        .writeValueAsString(grantsService.getOrganizationDataOverTime(organization, field)));
  }

  @RequestMapping(value = "/locations", method = RequestMethod.GET)
  public void getDataForEachLocation(@RequestParam("year") String year,
      @RequestParam("field") String field, HttpServletResponse response) throws IOException {
    response.getWriter().write(
        new ObjectMapper().writeValueAsString(grantsService.getDataForEachLocation(year, field)));
  }

}
