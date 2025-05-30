# Use Amazon Corretto for a slim, secure Java runtime
FROM amazoncorretto:24-alpine

# Set work directory
WORKDIR /app

# Copy the release JAR from build context
COPY build/libs/vehicle-0.0.1-SNAPSHOT.jar app.jar

# Expose the default port (adjust if your app uses a different port)
EXPOSE 50080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
