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
// See note about why I'm using JDBC instead of aws RDS data api: chat-gpt-notes/rds-data-api-chat-gpt-note.md
@Configuration
@EnableJpaRepositories(
    basePackages = "com.michael_gregory.blog_api.dao.users",  // your repository package
    entityManagerFactoryRef = "usersEntityManagerFactory",
    transactionManagerRef = "usersTransactionManager"
)
public class UsersDataSourceConfig {
    @Value("${spring.datasource.users.jdbc-url}")
    private String jdbcUrlTemplate; // e.g. "jdbc:postgresql://{HOST}:{PORT}/blogdb?sslmode=require"

    @Value("${spring.datasource.users.username}")
    private String username;

    // We'll assume the endpoint and port are defined in the URL template or separately.
    // For simplicity, we hardcode them here (replace with properties as needed).
    private final String host = "tuabt2pt3x4bvv6ywa2y2wm6n4.dsql.us-east-1.on.aws"; // Replace with your actual endpoint
    private final int port = 5432;

    // You can also inject the AWS region from properties if desired
    @Value("${aws.region:us-east-1}")
    private String awsRegion;

    @Bean(name = "usersDataSource")
    //@ConfigurationProperties(prefix = "spring.datasource.users")
    public DataSource userDataSource() {
        // Create an instance of the token generator
        //AuroraAuthTokenGenerator tokenGenerator =
        //        new AuroraAuthTokenGenerator(host, port, username, Region.of(awsRegion));
        // Generate the initial token for connection creation
        String authToken = GenerateAuthToken.generateToken(host, Region.of(awsRegion));
        //String authToken = tokenGenerator.generateToken();
        //"tuabt2pt3x4bvv6ywa2y2wm6n4.dsql.us-east-1.on.aws:5432/?DBUser=admin&Action=connect&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20250209T193128Z&X-Amz-SignedHeaders=host&X-Amz-Expires=900&X-Amz-Credential=AKIAQ3EGVABA6QI7RG6U%2F20250209%2Fus-east-1%2Frds-db%2Faws4_request&X-Amz-Signature=f308ab077fcb5b9a3ca777bfcb0556b2ae6f7665b8b4e8f999820e84ff5d54a3"
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
        // Optionally, you can set connection timeout, idle timeout, etc.
        
        return new HikariDataSource(config);
    }

        @Bean(name = "usersEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean usersEntityManagerFactory(
            @Qualifier("usersDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("com.michael_gregory.blog_api.entity.users"); // entities location
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emf.setPersistenceUnitName("usersPU");
        return emf;
    }

    @Bean(name = "usersTransactionManager")
    public PlatformTransactionManager usersTransactionManager(
            @Qualifier("usersEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    //     // Expose the shared EntityManager bean for Spring Data JPA repositories
    // @Bean(name = "jpaSharedEM_usersEntityManagerFactory")
    // public SharedEntityManagerBean sharedEntityManager(
    //         @Qualifier("usersEntityManagerFactory") EntityManagerFactory emf) {
    //     SharedEntityManagerBean sem = new SharedEntityManagerBean();
    //     sem.setEntityManagerFactory(emf);
    //     return sem;
    // }
}

