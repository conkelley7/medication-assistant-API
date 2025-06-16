package com.kelley.medicationassistant.security.service;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * A filter that intercepts each HTTP request to authenticate users based on
 * a JSON Web Token (JWT) included in the request header. The filter extracts
 * and validates the JWT, retrieves user details, and sets authentication in
 * the security context if the token is valid.
 */
@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private static final Logger authTokenFilterLogger = LoggerFactory.getLogger(AuthTokenFilter.class);

    public AuthTokenFilter(JwtService jwtService, UserDetailsServiceImpl userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Processes incoming HTTP requests to authenticate users based on the presence
     * of a JWT in the request. The method attempts to extract, validate,
     * and parse the JWT. If the token is valid, it retrieves the user details, sets up
     * the authentication object, and stores it in the Security Context.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        authTokenFilterLogger.debug("AuthTokenFilter called for URI: {}", request.getRequestURI());
        try {
            String jwtFullString = request.getHeader(HttpHeaders.AUTHORIZATION);
            String jwt = null;

            if (jwtFullString != null) {
                jwt = jwtFullString.substring(7).trim();
            }

            if (jwt != null && jwtService.validateJwtToken(jwt)) {
                String username = jwtService.getUsernameFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null,
                                userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }
}
