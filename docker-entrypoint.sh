#!/bin/bash
set -e

# Wait for DB to be ready (you can enhance this)
echo "⏳ Waiting for MySQL at ${DB_HOST}:${DB_PORT}..."
until mysqladmin ping -h"$DB_HOST" -P"$DB_PORT" --silent; do
  sleep 2
done
echo "✅ MySQL is ready."

# Run Flyway migration
flyway -url=jdbc:mysql://localhost:3307/food_ordering_system_db?createDatabaseIfNotExist=true\
       -user=root \
       -password=root \
       migrate

# Start the Spring Boot application
exec java -jar app.jar
