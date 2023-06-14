package com.omnicommerce.token.util;

import com.omnicommerce.golobal.exception.ErrorCodes;
import com.omnicommerce.user.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Log4j2
public class JwtFilter extends OncePerRequestFilter {
    private final Set<String> shouldNotFilterPaths;
    private UserDetailsService userDetailsService;

    public JwtFilter(Set<String> shouldNotFilterPaths, UserDetailsService userDetailsService) {
        this.shouldNotFilterPaths = shouldNotFilterPaths;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("Filter request: ", request.getServletPath());
        String token;
        try {
            token = TokenUtil.extractTokenFromHeader(request.getHeader(HttpHeaders.AUTHORIZATION));
        } catch (Exception e) {
            throw new ServletException(e);
        }

        if (!TokenUtil.parseJwt(token)) throw new ServletException();

        String username = TokenUtil.getSubject(token);
        log.debug(username);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (userDetails == null) throw new ServletException(ErrorCodes.E00003.getMessage());
        User user = (User) userDetails;

        SecurityContext ctx = SecurityContextHolder.getContext();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        ctx.setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();

        return this.shouldNotFilterPaths.contains(path);
    }
}
