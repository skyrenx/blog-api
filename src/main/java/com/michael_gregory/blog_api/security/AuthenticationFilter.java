package com.michael_gregory.blog_api.security;

import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
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
import com.michael_gregory.blog_api.entity.User;
import com.michael_gregory.blog_api.service.JwtService;

@Component
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private final SecurityConstants securityConstants;
    private final JwtService jwtService;

    public AuthenticationFilter(AuthenticationManager authenticationManager, SecurityConstants securityConstants, JwtService jwtService) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.securityConstants = securityConstants;
        this.jwtService = jwtService;
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
protected void successfulAuthentication(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain filterChain,
                                        Authentication authResult) throws IOException {
    String username = authResult.getName();
    List<String> roles = authResult.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());
    
    ResponseCookie cookie = jwtService.buildHttpOnlyCookie(jwtService.generateToken(username, roles));

    response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    // Prepare JSON response body
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> responseBody = Map.of(
            "username", authResult.getName(),
            "roles", roles
    );

    String jsonResponseBody = mapper.writeValueAsString(responseBody);
    response.getWriter().write(jsonResponseBody);
    response.getWriter().flush();

    // Do NOT call filterChain.doFilter here because the response is already committed (sent to the client) 
    // and we don't want to continue the filter chain.

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
