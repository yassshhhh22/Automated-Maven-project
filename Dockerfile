FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/todo-app-1.0-SNAPSHOT-jar-with-dependencies.jar /app/todo-app.jar

# Create logs directory
RUN mkdir -p /app/logs

# Expose the port if your app has a web interface
# EXPOSE 8080

# Run the application
CMD ["java", "-jar", "todo-app.jar"] 