#!/bin/bash

# Import environment variables if .env file exists
if [ -f .env ]; then
  export $(grep -v '^#' .env | xargs)
else
  echo "Warning: .env file not found. Using default environment variables."
fi

# Check if the database is running
if ! nc -z localhost 5432 > /dev/null 2>&1; then
  echo "Starting PostgreSQL database..."
  docker-compose up -d postgres
  
  # Wait for the database to be ready
  echo "Waiting for database to be ready..."
  timeout=30
  while ! nc -z localhost 5432 && [ $timeout -gt 0 ]; do
    sleep 1
    timeout=$((timeout-1))
  done
  
  if [ $timeout -eq 0 ]; then
    echo "Error: Database did not start in time"
    exit 1
  fi
fi

# Build and run the application
echo "Starting Chat Application..."
./gradlew bootRun