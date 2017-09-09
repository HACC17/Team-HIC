package gov.ehawaii.hacc.web.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.ehawaii.hacc.service.GrantsService;

/**
 * This controller handles all requests going to the <code>/charts</code> endpoint. The methods in this
 * class return data in JSON format that clients can use to generate charts using ChartJS or Highcharts.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
@Controller
@RequestMapping("/charts")
public class ChartsController {

  @Autowired
  private GrantsService grantsService;

  /**
   * This method returns aggregate data for the top 5 organizations for the given fiscal year.
   * 
   * @param year The fiscal year.
   * @param response The server response.
   * @throws IOException If there are problems sending the data back to the client.
   */
  @RequestMapping(value = "/fiscalYear", method = RequestMethod.GET)
  public void getTopFiveOrganizationsForFiscalYear(@RequestParam("year") String year,
      HttpServletResponse response) throws IOException {
    response.getWriter().write(new ObjectMapper()
        .writeValueAsString(grantsService.getTopFiveOrganizationsForFiscalYear(year)));
  }

  /**
   * This method returns the top N organizations, projects, locations, etc. by the given type of data.
   * 
   * @param top N, a number greater than 0.
   * @param name Organization, project, location, etc.
   * @param aggregateField The type of data on which to aggregate.
   * @param response The server response.
   * @throws IOException If there are problems sending the data back to the client.
   */
  @RequestMapping(value = "/top", method = RequestMethod.GET)
  public void getTopNData(@RequestParam("top") String top, @RequestParam("field1") String name,
      @RequestParam("field2") String aggregateField, HttpServletResponse response)
      throws IOException {
    response.getWriter().write(new ObjectMapper().writeValueAsString(
        grantsService.getTopNData(Integer.parseInt(top), name, aggregateField)));
  }

  /**
   * This method returns time series data for a single organization over a period of time.
   * 
   * @param organization The name of the organization.
   * @param field The type of data to fetch.
   * @param response The server response.
   * @throws IOException If there are problems sending the data back to the client.
   */
  @RequestMapping(value = "/time", method = RequestMethod.GET)
  public void getOrganizationDataOverTime(@RequestParam("org") String organization,
      @RequestParam("field") String field, HttpServletResponse response)
      throws IOException {
    response.getWriter().write(new ObjectMapper()
        .writeValueAsString(grantsService.getOrganizationDataOverTime(organization, field)));
  }

  /**
   * This method returns aggregate data for all locations.
   * 
   * @param aggregateField The type of data on which to aggregate.
   * @param filter Condition that must be fulfilled.
   * @param filterValue The value for the filter.
   * @param response The server response.
   * @throws IOException If there are problems sending the data back to the client.
   */
  @RequestMapping(value = "/locations", method = RequestMethod.GET)
  public void getDataForEachLocation(@RequestParam("aggregateField") String aggregateField,
      @RequestParam("filter") String filter, @RequestParam("filterValue") String filterValue,
      HttpServletResponse response) throws IOException {
    response.getWriter().write(new ObjectMapper().writeValueAsString(
        grantsService.getAggregateDataForEachLocation(aggregateField, filter, filterValue)));
  }

  /**
   * This method returns the top N data for the given location for the given fiscal year.
   * 
   * @param top N, a number greater than 0.
   * @param year The fiscal year.
   * @param location The location.
   * @param field The type of data to fetch.
   * @param response The server response.
   * @throws IOException If there are problems sending the data back to the client.
   */
  @RequestMapping(value = "/fiscalYearTop", method = RequestMethod.GET)
  public void getTopNDataForEachLocation(@RequestParam("top") String top,
      @RequestParam("year") String year, @RequestParam("location") String location,
      @RequestParam("field") String field, HttpServletResponse response) throws IOException {
    Map<String, String> filters = new HashMap<>();
    filters.put("location", location);
    filters.put("fiscal", year);

    response.getWriter().write(new ObjectMapper().writeValueAsString(
        grantsService.getTopNDataForEachLocation(Integer.parseInt(top), field, filters)));
  }

}
