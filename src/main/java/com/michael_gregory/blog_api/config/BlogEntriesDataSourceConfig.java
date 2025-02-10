package com.michael_gregory.blog_api.config;

import com.michael_gregory.blog_api.aws.GenerateAuthToken;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import software.amazon.awssdk.regions.Region;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
    basePackages = "com.michael_gregory.blog_api.dao.blogEntries",  // Package containing blog repositories
    entityManagerFactoryRef = "blogEntriesEntityManagerFactory",
    transactionManagerRef = "blogEntriesTransactionManager"
)
public class BlogEntriesDataSourceConfig {

    // Properties from application.properties
    @Value("${spring.datasource.blog.jdbc-url}")
    private String jdbcUrlTemplate; // e.g. "jdbc:postgresql://{HOST}:{PORT}/blogdb?sslmode=require"

    @Value("${spring.datasource.blog.username}")
    private String username;

    // Hardcoded for now (ideally, externalize these as well)
    private final String host = "jmabt2ny7wo7znmjjgf4fht7xe.dsql.us-east-1.on.aws";
    private final int port = 5432;

    @Value("${aws.region:us-east-1}")
    private String awsRegion;

    @Bean(name = "blogEntriesDataSource")
    public DataSource blogEntriesDataSource() {
        // Create an instance of the token generator
        //AuroraAuthTokenGenerator tokenGenerator =
        //        new AuroraAuthTokenGenerator(host, port, username, Region.of(awsRegion));
        // Generate the initial token for connection creation
        //String authToken = tokenGenerator.generateToken();
        String authToken = GenerateAuthToken.generateToken(host, Region.of(awsRegion));
        // Replace placeholders in the JDBC URL if necessary
        String jdbcUrl = jdbcUrlTemplate.replace("{HOST}", host)
                                        .replace("{PORT}", String.valueOf(port));

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(authToken);
        // Set the driver class name from properties or hardcode it
        config.setDriverClassName("org.postgresql.Driver");
        // Set maximum lifetime to slightly less than token validity (15 minutes token; 14 minutes max lifetime)
        config.setMaxLifetime(14 * 60 * 1000L);
        return new HikariDataSource(config);
    }

    @Bean(name = "blogEntriesEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean blogEntriesEntityManagerFactory(
            @Qualifier("blogEntriesDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        // Set the package to scan for entities associated with blog entries.
        emf.setPackagesToScan("com.michael_gregory.blog_api.entity.blogEntries");
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emf.setPersistenceUnitName("blogEntriesPU");
        return emf;
    }

    @Bean(name = "blogEntriesTransactionManager")
    public PlatformTransactionManager blogEntriesTransactionManager(
            @Qualifier("blogEntriesEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    //     // Expose the shared EntityManager bean for Spring Data JPA repositories
    // @Bean(name = "jpaSharedEM_blogEntriesEntityManagerFactory")
    // public SharedEntityManagerBean sharedEntityManager(
    //         @Qualifier("blogEntriesEntityManagerFactory") EntityManagerFactory emf) {
    //     SharedEntityManagerBean sem = new SharedEntityManagerBean();
    //     sem.setEntityManagerFactory(emf);
    //     return sem;
    // }
}
