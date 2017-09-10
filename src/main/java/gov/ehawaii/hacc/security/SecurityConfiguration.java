package gov.ehawaii.hacc.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * This class configures the security of this application.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@ehawaii.gov>
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  /**
   * Configures the admin account.
   * 
   * @param auth The authentication manager builder.
   * @throws Exception If there are problems configuring the admin account.
   */
  @Autowired
  public final void configureGlobalSecurity(final AuthenticationManagerBuilder auth)
      throws Exception {
    auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN");
  }

  /** {@inheritDoc} */
  @Override
  protected final void configure(final HttpSecurity http) throws Exception {
    http.authorizeRequests().antMatchers("/", "/login").permitAll().antMatchers("/admin/**")
        .access("hasRole('ADMIN')").and().formLogin().loginPage("/login").and().exceptionHandling()
        .accessDeniedPage("/Access_Denied");
  }
}
