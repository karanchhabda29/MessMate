# MessMate Project

## Project Overview

This is a Spring Boot application named "MessMate," designed as a smart mess subscription and meal management system. The application allows users to manage their mess subscriptions and opt out of meals. It provides endpoints for user authentication, managing meal-offs (for today or custom date ranges), handling subscriptions, and administrative functions for user management and reporting.

The backend is built with Java and the Spring Boot framework, utilizing Spring Data JPA for database persistence, Spring Security for handling authentication and authorization with JWTs, and the Firebase Admin SDK for push notifications.

### Core Technologies
- **Backend:** Java, Spring Boot
- **Database:** Spring Data JPA (compatible with MySQL/PostgreSQL)
- **Authentication:** Spring Security, JSON Web Tokens (JWT)
- **Build Tool:** Apache Maven
- **API Documentation:** Springdoc OpenAPI (Swagger)
- **Utilities:** Lombok, ModelMapper
- **Notifications:** Firebase Cloud Messaging (FCM)
- **Scheduling:** Spring Boot Scheduling

### Architecture
The project follows a standard layered architecture:
- **`controllers`**: Expose RESTful API endpoints.
- **`services`**: Contain the core business logic.
- **`repositories`**: Handle data access using Spring Data JPA.
- **`entities`**: Define the database schema.
- **`dto`**: Data Transfer Objects for API communication.
- **`config`**: Application and security configuration.
- **`schedulers`**: Background tasks for subscription and meal management.
- **`advice`**: Global exception and response handling.

## Building and Running

This is a Maven project. Use the Maven wrapper (`mvnw`) included in the repository for all commands.

### Prerequisites
- Java 21 (or compatible)
- Set the following environment variables for the database connection:
  - `DB_URL`: The JDBC URL of your database (e.g., `jdbc:mysql://localhost:3306/messmate`)
  - `DB_USERNAME`: Your database username
  - `DB_PASSWORD`: Your database password

### Running the Application
To run the application, use the Spring Boot Maven plugin:

```bash
./mvnw spring-boot:run
```

The application will start on the default port (usually 8080).

### Testing the Application
To run the test suite:

```bash
./mvnw test
```

### API Documentation
Once the application is running, you can access the Swagger UI for API documentation and testing at:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Development Conventions

- **Code Style:** Standard Java conventions.
- **Data Transfer:** Use DTOs for all data exchange with the client to decouple the API from the internal data model.
- **Configuration:** Environment-specific properties are managed in `application-{profile}.properties` files (`dev`, `prod`). The main `application.properties` file relies on environment variables for sensitive data like database credentials and JWT secrets.
- **Authentication:** API endpoints are secured using JWTs. Requests to protected endpoints must include a `Authorization: Bearer <token>` header.
- **Error Handling:** A `GlobalExceptionHandler` is in place to provide consistent and structured error responses.
- **Boilerplate Reduction:** The project uses Lombok extensively to reduce boilerplate code for getters, setters, constructors, etc.
