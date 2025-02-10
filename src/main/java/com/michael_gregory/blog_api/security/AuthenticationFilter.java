package com.michael_gregory.blog_api.security;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.michael_gregory.blog_api.entity.users.User;

@Component
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private final SecurityConstants securityConstants;

    public AuthenticationFilter(AuthenticationManager authenticationManager, SecurityConstants securityConstants) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.securityConstants = securityConstants;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(),
                    user.getPassword(), Arrays.asList());
            authentication = authenticationManager.authenticate(authentication);
            return authentication;
        } catch (IOException ex) {
            throw new RuntimeException();
            // TODO
            // This exception is being thrown in the Spring Security filter chain and cannot
            // be handled by
            // Spring Boot's global exception handlers. This is because exceptions that
            // occur during the
            // authentication or authorization process are managed by Spring Security's own
            // exception
            // handling mechanism, which operates outside of the standard Spring MVC
            // exception handling flow.
            // As a result, any exceptions thrown here will not be caught by the
            // @ControllerAdvice
            // annotated classes designed to handle application-level exceptions.

        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.addHeader("Content-Type", "application/json");
        response.getWriter().write("\""+failed.getMessage()+"\"");
        response.getWriter().flush();
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain,
            Authentication authResult) throws IOException, ServletException {
        List<String> roles = authResult.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());
        String token = JWT.create()
                .withSubject(authResult.getName())
                .withClaim("roles", roles) 
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.TOKEN_EXPIRATION))
                .sign(Algorithm.HMAC512(securityConstants.getSecretKey()));

        response.addHeader(SecurityConstants.AUTHORIZATION, SecurityConstants.BEARER + token);
        response.setContentType("application/json");
        //Do not do filterChain.doFilter here. I tried it and it caused more issues.
        ObjectMapper mapper = new ObjectMapper();
        
        AuthenticationSuccessResponseBody responseBody = new AuthenticationSuccessResponseBody(authResult.getName(), roles);
     
        // Convert to JSON string
        String jsonResponseBody = mapper.writeValueAsString(responseBody);
        response.getWriter().write(jsonResponseBody); 
        response.getWriter().flush();
        
    }

    class AuthenticationSuccessResponseBody {
        private String user;
        private List<String> roles;

        public AuthenticationSuccessResponseBody(String user, List<String> roles){
            this.user = user;
            this.roles = roles;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }

        
    }

}
