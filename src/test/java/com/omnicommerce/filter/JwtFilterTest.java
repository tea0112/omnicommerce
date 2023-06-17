package com.omnicommerce.filter;

import com.omnicommerce.token.util.TokenUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtFilterTest {
  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
  }

  @Mock
  private UserDetailsService userDetailsService;
  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpServletResponse response;
  @Mock
  private FilterChain filterChain;

  @InjectMocks
  private JwtFilter jwtFilter;

  @Test
  public void testDoFilterInternalSuccess() {
    String username = "myusername";
    String token = TokenUtil.generateToken(username);
    when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);
    when(userDetailsService.loadUserByUsername(username))
        .thenReturn(
            new User(
                username, "", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))));
    verify(filterChain, times(1));
  }
}
