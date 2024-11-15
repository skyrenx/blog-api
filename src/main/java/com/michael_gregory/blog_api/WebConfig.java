package com.michael_gregory.blog_api;

import java.util.Arrays;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
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

@Value("${cors.allowedOrigins}")
private String[] allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("") 
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Authorization", "Content-Type", "X-XSRF-TOKEN")
                .exposedHeaders("Authorization", "Content-Type", "X-XSRF-TOKEN")
                .allowCredentials(true); //TODO security
    }

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> servletContainer() {
        return factory -> {
            // Define the additional connector for HTTP on port 8080
            factory.addAdditionalTomcatConnectors(createStandardConnector());
        };
    }

    private Connector createStandardConnector() {
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setPort(8080); // Set the additional port for HTTP
        return connector;
    }
}
