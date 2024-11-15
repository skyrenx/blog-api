Getting started with local development:
    
    Before running the application:
        Start docker desktop.
        Execute run-mysql-container.sh to start a container in docker that will serve as a dev mysql database.
        Create a Development-Only Self-Signed Certificate as keystore.p12 and places in /resources...
        Use this command:
        keytool -genkeypair -alias devapp -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore keystore.p12 -validity 365
        Don't publish the certificate to a public repo.

    Run the application:
        mvn spring-boot:run

    Clean up:
        Execute stop-mysql-container.sh to terminate the docker container running the dev mysql database.

Helpful tools for local development:
    Run with environment variables set...
    mvn spring-boot:run -Dspring-boot.run.arguments="--CORS_ALLOWED_ORIGINS=https://localhost:3000,https://michaelgregory.dev --spring.profiles.active=dev"

    MySQL workbench


Deployment instructions:
    Package the application:
        Run the command "mvn package -Dspring.profiles.active=dev" to generate .jar file in /target directory
    Build docker image:
        Use your dockerfile to build a docker image with the command: docker build -t michael-gregory/blog-api .
    Upload the Image to Amazon Elastic Container Registry (ECR):
        Go to the ECR Console in AWS.
        Create a new repository and note the repository URI (e.g., 123456789012.dkr.ecr.us-west-2.amazonaws.com/yourapp).
    //deploy that image with production enviornment variables as an amazon lightsail container

    more TBD...