package com.michael_gregory.blog_api.aws;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.services.dsql.DsqlUtilities;
import software.amazon.awssdk.regions.Region;

public class GenerateAuthToken { 
    public static String generateToken(String yourClusterEndpoint, String awsRegion) {
        DsqlUtilities utilities = DsqlUtilities.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();

        // Use `generateDbConnectAuthToken` if you are _not_ logging in as `admin` user 
        String token = utilities.generateDbConnectAdminAuthToken(builder -> {
            builder.hostname(yourClusterEndpoint)
                    .region(Region.of(awsRegion));
        });

        return token;
    }
}
/* 
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rds.RdsClient;
import software.amazon.awssdk.services.rds.RdsUtilities;

//TODO: Alternatively, use aws RDS data api for simplified database connections. 
// For now, I'm dynaimcally generating auth tokens to maek a traditional jdbc connection
// https://github.com/awsdocs/aws-doc-sdk-examples/tree/main/javav2/usecases/Creating_Spring_RDS_Rest
public class AuroraAuthTokenGenerator {
    private final RdsUtilities rdsUtilities;
    private final String hostname;
    private final int port;
    private final String username;

    public AuroraAuthTokenGenerator(String hostname, int port, String username, Region region) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        // Build an RdsClient with your desired region and credentials.
        RdsClient rdsClient = RdsClient.builder()
                .region(region)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
        // Obtain RdsUtilities directly from the RdsClient.
        this.rdsUtilities = rdsClient.utilities();
    }

    public String generateToken() {
        return rdsUtilities.generateAuthenticationToken(builder ->
                builder.hostname(hostname)
                       .port(port)
                       .username(username)
        );
    }
}


*/