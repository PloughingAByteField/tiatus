package org.tiatus.server.filter;

import java.io.IOException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.Customizer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.web.csrf.CsrfFilter ;
import org.springframework.security.web.csrf.CsrfToken;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class ApplicationSecurity {

  @Autowired
  private DataSource dataSource;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
    requestHandler.setCsrfRequestAttributeName("_csrf");
    http
        .authorizeHttpRequests((authorize) -> authorize
            .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
            .requestMatchers("/rest/**").permitAll()
            .requestMatchers("/results/**").permitAll()
            .anyRequest().authenticated())
        .cors((cors) -> cors.disable())
        .csrf((csrf) -> csrf
			.csrfTokenRepository(getCsrfTokenRepository())
			.csrfTokenRequestHandler(requestHandler)
		)
		.addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)

        .headers(headers -> headers
          .cacheControl(cache -> cache.disable())
        )
        .httpBasic(Customizer.withDefaults())
        .formLogin((formLogin) -> formLogin.successHandler(successHandler()))
        .logout((logout) -> logout.logoutSuccessUrl("/rest/logout"));
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public JdbcUserDetailsManager jdbcUserDetailsManager() {
    JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
    jdbcUserDetailsManager.setUsersByUsernameQuery("select user_name, password, true from app_user where user_name=?");
    jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
        "select u.user_name, r.role from user_role ur, app_user u, role r where ur.user_id = u.id and ur.role_id = r.id and u.user_name=?");
    return jdbcUserDetailsManager;
  }

  @Bean
  AuthenticationSuccessHandler successHandler() {
    return new UserAuthenticationSuccessHandler();
  }

  private CsrfTokenRepository getCsrfTokenRepository() {
    CookieCsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
    csrfTokenRepository.setCookiePath("/");
    return csrfTokenRepository;
  }

  // see https://docs.spring.io/spring-security/reference/5.8/migration/servlet/exploits.html#_i_am_using_a_single_page_application_with_cookiecsrftokenrepository
  private static final class CsrfCookieFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
      CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
      // Render the token value to a cookie by causing the deferred token to be loaded
      csrfToken.getToken();

      filterChain.doFilter(request, response);
    }

  }
}