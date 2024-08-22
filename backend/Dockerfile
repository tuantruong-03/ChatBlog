# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:21.0.2_13-jre-jammy

# Set the working directory in the container
WORKDIR /app

# Copy the WAR file into the container at /app
COPY target/backend-0.0.1-SNAPSHOT.war /app/app.war

# Expose port 8080 to the outside world
EXPOSE 8080

# Set the command to run the WAR file
ENTRYPOINT ["java", "-jar", "/app/app.war"]
