package com.omnicommerce;

import com.omnicommerce.filter.JwtFilter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
public class CustomSecurity {
  private final Set<String> ShouldNotFilterPaths =
      new HashSet<>(Arrays.asList("/api/users/login", "/api/users/signup", "/api/users/seed"));

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

    if (log.isDebugEnabled())
      http.authorizeHttpRequests().antMatchers("/api/users/seed").permitAll();

    http.authorizeHttpRequests(
        auth -> {
          auth.antMatchers("/api/users/signup").permitAll();
          auth.antMatchers("/api/users/login").permitAll();
        });

    http.authorizeHttpRequests(
        auth -> {
          auth.antMatchers("/api/users/test-jwt").hasRole("USER");
        });

    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.addFilterBefore(
        new JwtFilter(this.ShouldNotFilterPaths, userDetailsService),
        UsernamePasswordAuthenticationFilter.class);

    http.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);

    return http.build();
  }
}
