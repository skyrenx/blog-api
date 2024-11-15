package com.michael_gregory.blog_api.security;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import org.springframework.web.cors.CorsConfigurationSource;

//TODO
//Test scenarios like invalid tokens, expired tokens, and unauthorized access attempts 
//to ensure that the filters handle these scenarios well.
//TODO input validation on user credentials during login...
//TODO store jwt in httponly cookie instead of local storage...
//TODO what is the JSESSIONID cookie that is getting sent on authentication
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${cors.allowedOrigins}")
    private String[] allowedOrigins;

    private CustomAuthenticationManager authenticationManager;

    // TODO @Lazy is required to resolve circular dependency issue. I still don't
    // completely understand the issue and should look into it further.
    public SecurityConfig(@Lazy CustomAuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Bean // TODO why
    UserDetailsManager UserDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager);
        authenticationFilter.setFilterProcessesUrl(SecurityConstants.LOGIN_PATH);

        http 
                .requiresChannel(channel -> channel
                        .requestMatchers(r -> r.getRequestURI().equals("/actuator/health")).requiresInsecure()
                        .anyRequest().requiresSecure() // Require HTTPS for all other requests
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .addFilterBefore(new ExceptionHandlerFilter(), AuthenticationFilter.class)
                .addFilter(authenticationFilter)
                .addFilterAfter(new JWTAuthorizationFilter(), AuthenticationFilter.class);
        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(Arrays.asList(allowedOrigins));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-XSRF-TOKEN"));
        config.setExposedHeaders(List.of("Authorization", "Content-Type", "X-XSRF-TOKEN"));
        config.setAllowCredentials(true); // needed for authorization header

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Apply this CORS configuration to all routes
        return source;
    }

    // Use this config if you want to enable CSRF later
    // // https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#csrf-integration-javascript-spa
    // .csrf(csrf -> csrf
    // .csrfTokenRepository(customCsrfTokenRepository())
    // .ignoringRequestMatchers("/api/public/**"))
    private CookieCsrfTokenRepository customCsrfTokenRepository() {
        //Store csrf token in a readable cookie
        CookieCsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        csrfTokenRepository.setCookiePath("/");
        csrfTokenRepository.setCookieName("XSRF-TOKEN");
        csrfTokenRepository.setCookieCustomizer((c) -> {
            c.secure(true).sameSite("None");
        }); // TODO don't use sameSite none in production...
        // Workaround to set SameSite=None attribute, as it's not directly configurable
        // on the repository
        csrfTokenRepository.setHeaderName("X-XSRF-TOKEN");

        return csrfTokenRepository;
    }

    //CORS config for use in dev
    //.cors(cors -> cors.configurationSource(devCorsConfigurationSource()))
    @Bean
    public CorsConfigurationSource devCorsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*"); // Allow any origin
        config.addAllowedMethod("*"); // Allow all HTTP methods
        config.addAllowedHeader("*"); // Allow all headers

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Apply this CORS configuration to all routes

        return source;
    }

}
