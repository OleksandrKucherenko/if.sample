FROM amazoncorretto:24-alpine

WORKDIR /app

COPY build/libs/insurance-*.jar insurance.jar

# Set the port insurance service will run on
EXPOSE 50081

# Health check to ensure the service is running properly
# HEALTHCHECK --interval=10s --timeout=3s --start-period=30s --retries=3 \
#  CMD wget -q --spider http://localhost:50081/insurances/ping || exit 1

# JVM configuration for better performance in containers
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Run the application
ENTRYPOINT ["java", "-jar", "insurance.jar"]
