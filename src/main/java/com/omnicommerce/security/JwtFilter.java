package com.omnicommerce.security;

import com.omnicommerce.golobal.exception.ErrorCodes;
import com.omnicommerce.golobal.exception.UserNotFoundException;
import com.omnicommerce.token.util.ServletUtil;
import com.omnicommerce.token.util.TokenUtil;
import com.omnicommerce.user.User;
import java.io.IOException;
import java.util.Arrays;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

@Log4j2
public class JwtFilter extends OncePerRequestFilter {
  public static final String[] excludedEndpoints =
      new String[] {
        "/api/users/login",
        "/api/users/signup",
        "/api/users/seed",
        "/v2/api-docs",
        "/swagger-ui",
        "/webjars",
        "/swagger-resources"
      };
  private final UserDetailsService userDetailsService;

  public JwtFilter(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    log.debug("JwtFilter starting");
    String token = null;
    try {
      token = TokenUtil.extractTokenFromHeader(request.getHeader(HttpHeaders.AUTHORIZATION));
    } catch (Exception ex) {
      ServletUtil.handleServletException(
          log, response, ErrorCodes.E00005.name(), ErrorCodes.E00005.getMessage(), ex);
    }

    try {
      TokenUtil.parseJwt(token);
    } catch (Exception ex) {
      ServletUtil.handleServletException(
          log, response, ErrorCodes.E00005.name(), ErrorCodes.E00005.getMessage(), ex);
      return;
    }

    String username = TokenUtil.getSubject(token);
    log.debug(username);
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

    if (userDetails == null) {
      ServletUtil.handleServletException(
          log,
          response,
          ErrorCodes.E00003.name(),
          ErrorCodes.E00003.getMessage(),
          new UserNotFoundException(ErrorCodes.E00003.getMessage()));
      return;
    }

    User user = (User) userDetails;

    SecurityContext ctx = SecurityContextHolder.getContext();
    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    ctx.setAuthentication(authentication);

    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String path = request.getServletPath();
    return Arrays.stream(excludedEndpoints).anyMatch(path::startsWith);
  }
}
