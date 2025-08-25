# --- Stage 1: Build the application with Maven ---
# This stage uses a full JDK and Maven to build the project.
FROM maven:3.9-eclipse-temurin-21 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project definition file first to leverage Docker's layer caching
COPY pom.xml .

# Copy the rest of your application's source code
COPY src ./src

# Run the Maven command to build the project and create the JAR file.
# This creates the /app/target/blog-api.jar file.
RUN mvn clean package -DskipTests


# --- Stage 2: Create the final, lightweight runtime image ---
# This stage uses the same lightweight Alpine image you had.
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory
WORKDIR /app

# This is the key step: Copy the JAR file from the 'builder' stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the port your application runs on
EXPOSE 8080

# The command to run your application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]