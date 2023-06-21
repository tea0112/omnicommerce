package com.omnicommerce.security;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Log4j2
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class CustomSecurity {
  private static final String[] AUTH_WHITELIST = {
      "/swagger-resources/**",
      "/swagger-ui.html",
      "/swagger-ui/**",
      "/v2/api-docs",
      "/webjars/**"
  };

  @Autowired
  @Qualifier("customAccessDeniedHandler")
  private AccessDeniedHandler customAccessDeniedHandler;

  @Autowired private UserDetailsService userDetailsService;

  @Bean
  public PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf().disable();

    if (log.isDebugEnabled()) {
      http.authorizeHttpRequests().antMatchers("/api/users/seed").permitAll();
      http.authorizeHttpRequests().antMatchers(AUTH_WHITELIST).permitAll();
    }

    http.authorizeHttpRequests(
        auth -> {
          auth.antMatchers("/api/users/signup").permitAll();
          auth.antMatchers("/api/users/login").permitAll();
        });

    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.addFilterBefore(
        new JwtFilter(userDetailsService),
        UsernamePasswordAuthenticationFilter.class);

    http.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);

    return http.build();
  }
}
