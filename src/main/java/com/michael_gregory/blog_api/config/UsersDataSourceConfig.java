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

import java.util.HashMap;
import java.util.Map;

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
    public DataSource userDataSource() {
        String jdbcUrl = jdbcUrlTemplate.replace("{HOST}", host).replace("{PORT}", String.valueOf(port));
        return new RefreshingAuroraDataSource(jdbcUrl, "admin", host, Region.of(awsRegion));
    }

        @Bean(name = "usersEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean usersEntityManagerFactory(
            @Qualifier("usersDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("com.michael_gregory.blog_api.entity.users"); // entities location
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emf.setPersistenceUnitName("usersPU");
        // Set the Hibernate dialect explicitly
        Map<String, Object> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        emf.setJpaPropertyMap(jpaProperties);
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

