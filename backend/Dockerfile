FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the jar file into the container
COPY target/TaskManager-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Specify the command to run on container start
ENTRYPOINT ["java", "-jar", "app.jar"]