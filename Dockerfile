# ---- Stage 1: Build the application ----
# Use a Maven image to build the project. Match the Java version (e.g., 17).
FROM maven:3.9-amazoncorretto-17 AS builder

# Set the working directory
WORKDIR /app

# Copy the Maven project file first to leverage Docker layer caching
COPY pom.xml .

# Copy the rest of your source code
COPY src ./src

# Build the project and create the JAR file. Skip tests to speed up the build.
RUN mvn clean package -DskipTests


# ---- Stage 2: Create the final, lightweight image ----
# Use a slim JRE (Java Runtime Environment) image, not a full JDK.
FROM amazoncorretto:17-al2-jre

# Set the working directory
WORKDIR /app

# Copy ONLY the built JAR file from the 'builder' stage
COPY --from=builder /app/target/homehub-backend-*.jar app.jar

# Expose the port your application runs on
EXPOSE 8080

# The command to run your application
ENTRYPOINT ["java", "-jar", "app.jar"]