package com.omnicommerce;

import com.omnicommerce.token.util.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class CustomSecurity {
    @Autowired
    private UserDetailsService userDetailsService;
    private final Set<String> ShouldNotFilterPaths = new HashSet<>(Arrays.asList("/api/users/login", "/api/users/signup"));

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.authorizeHttpRequests(auth -> {
            auth.antMatchers("/api/users/signup").permitAll();
            auth.antMatchers("/api/users/login").permitAll();
        });

        http.authorizeHttpRequests(auth -> {
            auth.antMatchers("/api/users/test-jwt").hasRole("ADMIN");
        });

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(new JwtFilter(this.ShouldNotFilterPaths, userDetailsService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
