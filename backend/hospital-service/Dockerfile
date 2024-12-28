# Use a base image with Java 17
FROM openjdk:17-jdk-slim

# Define an argument for the JAR file path
ARG JAR_FILE=target/*.jar

# Copy the JAR file into the container
COPY ${JAR_FILE} app.jar

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]