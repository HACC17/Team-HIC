package gov.ehawaii.hacc.web.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.ehawaii.hacc.service.GrantsService;

@Controller
public class MainController {

  @Autowired
  private GrantsService grantsService;

  @PostConstruct
  public void init() {
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String showIndexPage(Model model) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("fiscal-gte", "2016");
    parameters.put("fiscal-lte", "2016");
    parameters.put("amount-gte", 100000);
    parameters.put("amount-lte", 1000000);
    parameters.put("hawaiians-gte", 0);
    parameters.put("hawaiians-lte", 1000);
    parameters.put("total-gte", 0);
    parameters.put("total-lte", 1000);
    model.addAttribute("all", grantsService.getGrants(parameters));
    model.addAttribute("statuses", grantsService.getAllGrantStatuses());
    model.addAttribute("types", grantsService.getAllGrantTypes());
    model.addAttribute("organizations", grantsService.getAllOrganizations());
    model.addAttribute("projects", grantsService.getAllProjects());
    model.addAttribute("locations", grantsService.getAllLocations());
    model.addAttribute("priorities", grantsService.getAllStrategicPriorities());
    model.addAttribute("results", grantsService.getAllStrategicResults());
    return "index";
  }

  @RequestMapping(value = "/charts", method = RequestMethod.GET)
  public String showChartsPage(Model model) {
    model.addAttribute("years", grantsService.getAllFiscalYears());
    model.addAttribute("organizations", grantsService.getAllOrganizations());
    return "charts";
  }

  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public String showLoginPage() {
    return "login";
  }

  @RequestMapping(value = "/filter", method = RequestMethod.POST)
  public void getDataForFiscalYear(@RequestBody String json, HttpServletResponse response)
      throws IOException {
    Map<String, Object> parameters =
        new ObjectMapper().readValue(json, new TypeReference<Map<String, Object>>() {
        });
    response.getWriter()
        .write(new ObjectMapper().writeValueAsString(grantsService.getGrants(parameters)));
  }

}
