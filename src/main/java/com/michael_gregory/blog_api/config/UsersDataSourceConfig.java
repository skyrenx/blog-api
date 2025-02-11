package com.michael_gregory.blog_api.config;

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

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;
// See note about why I'm using JDBC instead of aws RDS data api: chat-gpt-notes/rds-data-api-chat-gpt-note.md
@Configuration
@EnableJpaRepositories(
    basePackages = "com.michael_gregory.blog_api.dao.users",
    entityManagerFactoryRef = "usersEntityManagerFactory",
    transactionManagerRef = "usersTransactionManager"
)
public class UsersDataSourceConfig {
    @Value("${spring.datasource.users.jdbc-url}")
    String jdbcUrl;
    @Value("${spring.datasource.users.username}")
    String username;
    @Value("${spring.datasource.users.host}")
    String clusterEndpoint;
    @Value("${aws.region}")
    String region;

    @Bean(name = "usersDataSource")
    public DataSource userDataSource() {
        return new RefreshingAuroraDataSource(jdbcUrl, username, clusterEndpoint, region);
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

