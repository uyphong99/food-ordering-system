# Use an official OpenJDK 17 runtime as the base image
FROM eclipse-temurin:17-jre-alpine

RUN mkdir /app
# Set the working directory in the container
WORKDIR /app

# Copy your Java application JAR file into the container
COPY target/app-2-1.0-SNAPSHOT.jar /app/app-2-1.0-SNAPSHOT.jar

# Specify the command to run your Java application
CMD ["java", "-jar", "/app/app-2-1.0-SNAPSHOT.jar"]