package gov.ehawaii.hacc.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * This controller logs out the user from the application.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
@Controller
public class LogoutController {

  /**
   * A <code>GET</code> request sent to <code>/logout</code> endpoint will log out the user from the application.
   * 
   * @param request The client request.
   * @param response The server response.
   * @return Redirects the user to the main page of the application.
   */
  @RequestMapping(value = "/logout", method = RequestMethod.GET)
  public final String logout(final HttpServletRequest request, final HttpServletResponse response) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      new SecurityContextLogoutHandler().logout(request, response, authentication);
    }
    return "redirect:/";
  }

}
