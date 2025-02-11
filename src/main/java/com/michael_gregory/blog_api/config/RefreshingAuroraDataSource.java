package com.michael_gregory.blog_api.config;

import com.michael_gregory.blog_api.aws.GenerateAuthToken;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;


public class RefreshingAuroraDataSource extends HikariDataSource {

    private String clusterEndpoint;
    private String region;

    public RefreshingAuroraDataSource(String jdbcUrl, String username, String clusterEndpoint, String region) {
        super();
        this.setJdbcUrl(jdbcUrl);
        this.setUsername(username);
        this.clusterEndpoint = clusterEndpoint;
        this.region = region;

        // Set an initial token
        this.setPassword(GenerateAuthToken.generateToken(clusterEndpoint, username));
        this.setDriverClassName("org.postgresql.Driver");
        // Set the maxLifetime to less than 15 minutes (e.g., 14 minutes)
        this.setMaxLifetime(14 * 60 * 1000L);
        // Optionally configure connection timeout, minimum idle, etc.
        
    }

    @Override
    public Connection getConnection() throws SQLException {
        // Before returning a new connection, refresh the password (auth token)
        this.setPassword(GenerateAuthToken.generateToken(clusterEndpoint, region));
        return super.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        // Use the fresh token here as well
        this.setPassword(GenerateAuthToken.generateToken(clusterEndpoint, region));
        return super.getConnection(username, this.getPassword());
    }
}
