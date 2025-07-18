FROM amazoncorretto:23
WORKDIR /app

# Copy the JAR file (updated pattern to match your build)
COPY target/homehub-backend-*.jar app.jar

# Expose the correct port (matches your application.yml)
EXPOSE 8081

# Add health check
HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8081/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]