# Chat Application

A simple real-time chat application built with Spring Boot and WebSockets.

## Features

- Real-time messaging using WebSockets
- Users can join with a username
- Multiple chat rooms (create new or join existing)
- Room-specific chat messages
- PostgreSQL database for message persistence
- Responsive web UI

## Technology Stack

- Java 21
- Spring Boot 3.4.5
- Spring WebSocket for real-time communication
- Spring Data JPA for database access
- PostgreSQL for message storage
- Flyway for database migrations
- Thymeleaf for server-side rendering
- SockJS and STOMP for WebSocket client

## Project Structure

- `model`: Contains the Message class representing chat messages
- `service`: Contains the ChatService for in-memory message storage
- `controller`: Contains the ChatController handling HTTP and WebSocket requests
- `config`: Contains WebSocket configuration
- `resources/templates`: HTML templates for the UI
- `resources/static`: Static resources (CSS, JavaScript)

## Running the Application

### Prerequisites

- Java 21 or later
- Gradle
- Docker and Docker Compose (for PostgreSQL database)

### Environment Setup

1. Copy the example environment file to create your own:

```
cp .env.example .env
```

2. Edit the `.env` file with your preferred database credentials.

### Database Setup

Start the PostgreSQL database using Docker Compose:

```
docker-compose up -d
```

### Build and Run

```
./gradlew build
./gradlew bootRun
```

The application will be available at http://localhost:8080

### Default Credentials

The application is initialized with a default administrator account:
- Username: admin
- Password: password (this should be changed in production)

## Testing

The application includes both unit tests and integration tests:

- Unit tests for model, service, and controller
- Integration tests for the application
- WebSocket integration tests

Run the tests with:

```
./gradlew test
```

## Development

This project uses Gradle as the build tool. Here are some useful Gradle commands:

- `./gradlew bootRun` - Run the application
- `./gradlew build` - Build the application
- `./gradlew test` - Run tests
- `./gradlew clean` - Clean build artifacts