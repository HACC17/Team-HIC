package gov.ehawaii.hacc.web.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.ehawaii.hacc.service.GrantsService;

/**
 * This controller handles all requests going to the <code>/charts</code> endpoint. The methods in
 * this class return data in JSON format that clients can use to generate charts using ChartJS or
 * Highcharts.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
@Controller
@RequestMapping("/charts")
public class ChartsController {

  private static final Logger LOGGER = LogManager.getLogger(MainController.class);

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
  public final void getTopFiveOrganizationsForFiscalYear(@RequestParam("year") final String year,
      final HttpServletResponse response) throws IOException {
    response.getWriter().write(new ObjectMapper()
        .writeValueAsString(grantsService.getTopFiveOrganizationsForFiscalYear(year)));
  }

  /**
   * This method returns the top N organizations, projects, locations, etc. by the given type of
   * data.
   * 
   * @param top N, a number greater than 0.
   * @param name Organization, project, location, etc.
   * @param aggregateField The type of data on which to aggregate.
   * @param response The server response.
   * @throws IOException If there are problems sending the data back to the client.
   */
  @RequestMapping(value = "/top", method = RequestMethod.GET)
  public final void getTopNData(@RequestParam("top") final String top,
      @RequestParam("field1") final String name,
      @RequestParam("field2") final String aggregateField, final HttpServletResponse response)
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
  public final void getOrganizationDataOverTime(@RequestParam("org") final String organization,
      @RequestParam("field") final String field, final HttpServletResponse response)
      throws IOException {
    response.getWriter().write(new ObjectMapper()
        .writeValueAsString(grantsService.getOrganizationDataOverTime(organization, field)));
  }

  /**
   * This method returns aggregate data for all locations.
   * 
   * @param json
   * @param response The server response.
   * @throws IOException If there are problems sending the data back to the client.
   */
  @RequestMapping(value = "/totals", method = RequestMethod.POST)
  public final void getTotals(@RequestBody final String json, final HttpServletResponse response)
      throws IOException {
    LOGGER.info("JSON: " + json);
    Map<String, Object> parameters =
        new ObjectMapper().readValue(json, new TypeReference<Map<String, Object>>() {
        });
    String aggregateField = parameters.get("aggregateField").toString();
    String groupBy = parameters.get("groupBy").toString();
    @SuppressWarnings("unchecked")
    Map<String, Object> filters = (Map<String, Object>) parameters.get("filters");
    response.getWriter().write(new ObjectMapper()
        .writeValueAsString(grantsService.getAggregateData(groupBy, aggregateField, filters)));
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
  public final void getTopNDataForEachLocation(@RequestParam("top") final String top,
      @RequestParam("year") final String year, @RequestParam("location") final String location,
      @RequestParam("field") final String field, final HttpServletResponse response)
      throws IOException {
    Map<String, String> filters = new HashMap<>();
    filters.put("location", location);
    filters.put("fiscal", year);

    response.getWriter().write(new ObjectMapper().writeValueAsString(
        grantsService.getTopNDataForEachLocation(Integer.parseInt(top), field, filters)));
  }

}
