package com.michael_gregory.blog_api.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {
    private final SecurityConstants securityConstants;

    public JWTAuthorizationFilter(SecurityConstants securityConstants) {
        this.securityConstants = securityConstants;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();

        // 🔹 Skip login + public endpoints
        if (uri.equals(SecurityConstants.LOGIN_PATH) || uri.startsWith("/api/public")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = null;

        // 🔹 Check cookies for JWT
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("JWT".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }

        if (token != null) {
            try {
                // 🔹 Validate token
                DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(securityConstants.getSecretKey()))
                        .build()
                        .verify(token);

                String user = decodedJWT.getSubject();
                List<String> roles = decodedJWT.getClaim("roles").asList(String.class);

                Collection<SimpleGrantedAuthority> authorities = roles == null
                        ? List.of()
                        : roles.stream().map(SimpleGrantedAuthority::new).toList();

                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(user, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception ex) {
                // The security context holds the authentication information for the current user.
                // If the token is invalid, we want to make sure that no authentication information
                // is retained, so we clear the context to prevent any unauthorized access.
                // Example: If a user tries to access a protected resource with an invalid token,
                // clearing the security context ensures that they are treated as unauthenticated.
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}

