package com.michael_gregory.blog_api.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        
        if (request.getRequestURI().equals(SecurityConstants.LOGIN_PATH))
        {
            filterChain.doFilter(request, response); // Skip JWT validation for login
            return;
        }
        String header = request.getHeader("Authorization");
        
        //skip this filter if there is no jwt (Request is not attempting authorization)
        if (header == null || !header.startsWith(SecurityConstants.BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.replace(SecurityConstants.BEARER, "");
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(securityConstants.getSecretKey()))
        .build()
        .verify(token);
        String user =  decodedJWT.getSubject();

        List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
        Collection<SimpleGrantedAuthority> authorities = Arrays.asList();
        if(roles != null && !roles.isEmpty()){
            authorities = roles.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response); 
    }
}
