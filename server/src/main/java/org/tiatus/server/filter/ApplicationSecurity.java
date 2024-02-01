package org.tiatus.server.filter;

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

import jakarta.servlet.DispatcherType;

@Configuration
@EnableWebSecurity
public class ApplicationSecurity {

  @Autowired
  private DataSource dataSource;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    
    http
        .authorizeHttpRequests((authorize) -> authorize
            .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
            .requestMatchers("/rest/login").permitAll()
            .requestMatchers("/rest/logout").permitAll()
            .anyRequest().authenticated())
        // TODO add csrf support see https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#:~:text=JavaScript%20Applications,-JavaScript%20applications%20typically&text=In%20order%20to%20obtain%20the,as%20an%20HTTP%20request%20header.
        .csrf(AbstractHttpConfigurer::disable)
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
}