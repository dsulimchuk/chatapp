# Chat Application

A simple real-time chat application built with Spring Boot and WebSockets.

## Features

- Real-time messaging using WebSockets
- Users can join with a username
- All messages are visible to all connected users
- In-memory message storage
- Responsive web UI

## Technology Stack

- Java 21
- Spring Boot 3.4.5
- Spring WebSocket for real-time communication
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

### Build and Run

```
./gradlew build
./gradlew bootRun
```

The application will be available at http://localhost:8080

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