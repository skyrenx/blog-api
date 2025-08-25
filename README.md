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


Deployment instructions using lightsail container:
    Package the application:
        Run the command to generate .jar file in /target directory
            mvn package
    Build docker image:
        Use your dockerfile to build a docker image with the command: 
            docker build -t blog/api-repo .
    Upload the Image to Amazon Elastic Container Registry (ECR):
        Go to the ECR Console in AWS.
            aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 058264453185.dkr.ecr.us-east-1.amazonaws.com
        Create a tag in docker using the repo URI
            (UPDATE VERSION before tagging) docker tag blog/api-repo:latest 058264453185.dkr.ecr.us-east-1.amazonaws.com/blog/api-repo:1.9
        Push tagged docker image to ECR
            (UPDATE VERSION to the tag  you just created) docker push 058264453185.dkr.ecr.us-east-1.amazonaws.com/blog/api-repo:1.9
    Deploy that tagged image with production enviornment variables as an amazon lightsail container

    more TBD...