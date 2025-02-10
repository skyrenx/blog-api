package com.michael_gregory.blog_api.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.michael_gregory.blog_api.exception.EntityNotFoundException;

public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (EntityNotFoundException e) { //Feel free to create a separate function.
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Username doesn't exist");
            response.getWriter().flush();
        } catch (JWTVerificationException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("JWT NOT VALID");
            response.getWriter().flush();
        } catch (AccessDeniedException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("ACCESS DENIED");
            response.getWriter().flush();
        } catch (RuntimeException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String stackTrace = sw.toString();
            response.getWriter().write("BAD REQUEST: " + stackTrace);
            response.getWriter().flush();
        }  
    }
}
