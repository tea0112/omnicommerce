package com.omnicommerce.token.util;

import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class JwtFilter extends OncePerRequestFilter {
    private final Set<String> shouldNotFilterPaths;

    public JwtFilter(Set<String> shouldNotFilterPaths) {
        this.shouldNotFilterPaths = shouldNotFilterPaths;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token;
        try {
            token = TokenUtil.extractTokenFromHeader(request.getHeader(HttpHeaders.AUTHORIZATION));
        } catch (Exception e) {
            throw new ServletException(e);
        }

        if (!TokenUtil.parseJwt(token)) throw new ServletException();

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();

        return this.shouldNotFilterPaths.contains(path);
    }
}
