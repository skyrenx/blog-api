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

    MySQL workbench


Deployment instructions:
    TBD