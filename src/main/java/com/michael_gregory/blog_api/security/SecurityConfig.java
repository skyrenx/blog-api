package com.michael_gregory.blog_api.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.michael_gregory.blog_api.service.AuthorityService;
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /*
     * UserController(userServiceImpl);
     * SecurityConfig(authManager);
     * AuthManager(userServiceImpl);
     * 
     * 
     */

    private CustomAuthenticationManager authenticationManager;

    //TODO @Lazy is required to resolve circular dependency issue. I still don't 
    // completely understand the issue and should look into it further.
    public SecurityConfig(@Lazy CustomAuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    @Bean
    UserDetailsManager UserDetailsManager(DataSource dataSource){
        return new JdbcUserDetailsManager(dataSource);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager);
        authenticationFilter.setFilterProcessesUrl("/api/user/login");
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/public/*").permitAll()
                        //.requestMatchers(HttpMethod.POST, "/api/user/register").permitAll()
                        //.requestMatchers("/api/admin/*").hasRole("ADMIN")
                        .requestMatchers("/api/admin/*").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .addFilterBefore(new ExceptionHandlerFilter(), AuthenticationFilter.class)
                .addFilter(authenticationFilter)
                .addFilterAfter(new JWTAuthorizationFilter(), AuthenticationFilter.class);


        //TODO
        //Test scenarios like invalid tokens, expired tokens, and unauthorized access attempts 
        //to ensure that the filters handle these gracefully.

        // TODO disable Cross Site Request Forgery (CSRF)
        // in general, not required for stateless REST APIs that use POST, PUT, DELETE
        // and/or PATCH
        http.csrf(csrf -> csrf.disable());

        return http.build(); // Build the security filter chain
    }


}