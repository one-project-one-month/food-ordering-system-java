# ----------- Stage 1: Build Gradle project ----------- #
FROM gradle:8.5-jdk17 AS build

WORKDIR /app

# Copy Gradle project metadata
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle

# Download dependencies (use cache)
RUN ./gradlew dependencies --no-daemon || true

# Copy full source code
COPY . /app

# Optional: Disable Gradle build scan
RUN sed -i '/buildScan {/,/}/s/^/\/\/ /' /app/build.gradle

# Build Spring Boot JAR
RUN ./gradlew clean build -x test --no-daemon


# ----------- Stage 2: Runtime with Flyway CLI ----------- #
FROM openjdk:17-jdk-slim

WORKDIR /app

# Install Flyway CLI and MySQL client
RUN apt-get update && \
    apt-get install -y curl unzip default-mysql-client && \
    curl -L https://repo1.maven.org/maven2/org/flywaydb/flyway-commandline/9.22.1/flyway-commandline-9.22.1-linux-x64.tar.gz \
    | tar xz && \
    mv flyway-*/flyway /usr/local/bin/flyway && \
    rm -rf flyway-* && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# Copy the built JAR and migration scripts
COPY --from=build /app/build/libs/*.jar app.jar
COPY src/main/resources/db/migration /flyway/sql
COPY docker-entrypoint.sh /app/docker-entrypoint.sh
RUN chmod +x /app/docker-entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["/app/docker-entrypoint.sh"]