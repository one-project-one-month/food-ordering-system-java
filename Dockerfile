# Use a multi-stage build to build the app with Gradle
FROM gradle:8.5-jdk17 AS build

WORKDIR /app

# Copy Gradle files and download dependencies
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle
RUN ./gradlew dependencies --no-daemon || true

# Copy source code and build
COPY . /app

RUN ./gradlew clean build -x test --no-daemon

# Runtime image
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/build/libs/*.jar /app/app.jar

EXPOSE 8080

# Run the Spring Boot app directly (no custom entrypoint)
CMD ["java", "-jar", "/app/app.jar"]
