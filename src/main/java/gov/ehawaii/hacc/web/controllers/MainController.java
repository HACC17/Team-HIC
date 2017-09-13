package gov.ehawaii.hacc.web.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.ehawaii.hacc.model.Grant;
import gov.ehawaii.hacc.service.GrantsService;

/**
 * This controller handles all requests going to the main page of the application.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
@Controller
public class MainController {

  @Autowired
  private GrantsService grantsService;

  /**
   * Displays the main page of this application. A model is populated with lists that are used in a
   * filter panel to populate dropdown lists.
   * 
   * @param model Used to store lists of values that are used to populate dropdown lists on the main
   * page.
   * @return The main page.
   */
  @RequestMapping(value = "/", method = RequestMethod.GET)
  public final String showIndexPage(final Model model) {
    Map<String, Object> parameters = new HashMap<>();
    /*
     * parameters.put("fiscal-gte", new ArrayList<>(Arrays.asList("2016")));
     * parameters.put("fiscal-lte", new ArrayList<>(Arrays.asList("2016")));
     * parameters.put("amount-gte", new ArrayList<>(Arrays.asList("100000")));
     * parameters.put("amount-lte", new ArrayList<>(Arrays.asList("1000000")));
     * parameters.put("hawaiians-gte", new ArrayList<>(Arrays.asList("0")));
     * parameters.put("hawaiians-lte", new ArrayList<>(Arrays.asList("1000")));
     * parameters.put("total-gte", new ArrayList<>(Arrays.asList("0"))); parameters.put("total-lte",
     * new ArrayList<>(Arrays.asList("1000")));
     */
    model.addAttribute("all", grantsService.getGrants(parameters));
    model.addAttribute("statuses", grantsService.getAllGrantStatuses());
    model.addAttribute("types", grantsService.getAllGrantTypes());
    model.addAttribute("organizations", grantsService.getAllOrganizations());
    model.addAttribute("projects", grantsService.getAllProjects());
    model.addAttribute("locations", grantsService.getAllLocations());
    model.addAttribute("priorities", grantsService.getAllStrategicPriorities());
    model.addAttribute("results", grantsService.getAllStrategicResults());
    model.addAttribute("grant", new Grant());
    return "index";
  }

  /**
   * A <code>POST</code> request sent to the <code>/filter</code> endpoint <strong>must</strong>
   * contain a JSON object that contains a list of lists of filters in the request body.<br />
   * <br />
   * 
   * A list of grants that satisfy the conditions in the filters will be sent in the response back
   * to the client.
   * 
   * @param json A JSON string that contains the filters that will be used in the query to retrieve
   * grants.
   * @param response The response that will contain the list of grants that satisfy the conditions.
   * @throws IOException If problems are encountered while trying to parse the JSON string or get
   * the list of grants.
   */
  @RequestMapping(value = "/filter", method = RequestMethod.POST)
  public final void getGrants(@RequestBody final String json, final HttpServletResponse response)
      throws IOException {
    Map<String, Object> parameters =
        new ObjectMapper().readValue(json, new TypeReference<Map<String, Object>>() {
        });
    response.getWriter()
        .write(new ObjectMapper().writeValueAsString(grantsService.getGrants(parameters)));
  }

}
