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

@Configuration
@EnableJpaRepositories(
    basePackages = "com.michael_gregory.blog_api.dao.blogEntries",
    entityManagerFactoryRef = "blogEntriesEntityManagerFactory",
    transactionManagerRef = "blogEntriesTransactionManager"
)
public class BlogEntriesDataSourceConfig {
    @Value("${spring.datasource.blog.jdbc-url}")
    String jdbcUrl;
    @Value("${spring.datasource.blog.username}")
    String username;
    @Value("${spring.datasource.blog.host}")
    String clusterEndpoint;
    @Value("${aws.region}")
    String region;

    @Bean(name = "blogEntriesDataSource")
    public DataSource blogEntriesDataSource() {
        return new RefreshingAuroraDataSource(jdbcUrl, username, clusterEndpoint, region);
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

        // Set the Hibernate dialect explicitly
        Map<String, Object> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        emf.setJpaPropertyMap(jpaProperties);
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
