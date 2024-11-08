package com.michael_gregory.blog_api.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.michael_gregory.blog_api.entity.User;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter{

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            System.out.println(user.getUsername());
            System.out.println(user.getPassword());
        } catch (IOException ex) {
            throw new RuntimeException(); 
            //TODO
            // This exception is being thrown in the Spring Security filter chain and cannot be handled by
            // Spring Boot's global exception handlers. This is because exceptions that occur during the
            // authentication or authorization process are managed by Spring Security's own exception 
            // handling mechanism, which operates outside of the standard Spring MVC exception handling flow. 
            // As a result, any exceptions thrown here will not be caught by the @ControllerAdvice 
            // annotated classes designed to handle application-level exceptions.

        }
        return super.attemptAuthentication(request, response);
    }

}
