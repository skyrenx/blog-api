package com.michael_gregory.blog_api;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
// Question : Do I need two cors configurations? WebConfig cors mappings AND security cors configuration?
// This configuration applies to all requests handled by the Spring MVC controllers but does not 
// cover requests handled by Spring Security filters directly, such as those related to authentication.
// Even though the cors configuration set up in SecurityConfig is sufficient for all endpoints, there
// may be a case where an endpoint bypasses Spring security and this cors configuration would apply
// as a fallback. (health check endpoints may be an example)
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") 
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Authorization", "Content-Type", "X-XSRF-TOKEN")
                .exposedHeaders("Authorization", "Content-Type", "X-XSRF-TOKEN")
                .allowCredentials(true); //TODO security
    }
}
