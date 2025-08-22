# 🛍️ ShopSphere: A Microservices E-Commerce Platform

A complete e-commerce backend built with Java and Spring Boot, demonstrating a modern, event-driven microservices architecture using Spring Cloud, Eureka, and Apache Kafka.

---
## Technology Stack

-   **Backend**: Java 17+, Spring Boot 3.x, Spring Cloud, Spring Security
-   **Infrastructure**: Spring Cloud Gateway, Netflix Eureka (Service Discovery), Apache Kafka
-   **Data**: Spring Data JPA, Hibernate, PostgresSQL, MongoDB
-   **API**: REST, WebClient
-   **Build**: Maven

---
## Services Included

-   **service-registry**: Eureka server for service discovery.
-   **api-gateway**: Single entry point for all client requests.
-   **user-service**: Handles user registration and JWT authentication.
-   **product-catalog-service**: Manages products and inventory.
-   **order-service**: Handles write operations for orders (CQRS Command side).
-   **order-query-service**: Handles fast, read-only queries for order data (CQRS Query side).
-   **payment-service**: Simulates payment processing.
-   **notification-service**: Sends asynchronous email notifications.

---
## Core Design Patterns

-   **Microservices Architecture**: Splits the application into small, independent services.
-   **API Gateway**: Provides a single, unified entry point for all external clients.
-   **Service Discovery**: Allows services to find each other dynamically without hardcoded URLs.
-   **CQRS (Command Query Responsibility Segregation)**: Separates the models for writing data from the models for reading data to optimize performance and scalability.
-   **Event-Driven Communication**: Uses Apache Kafka to enable asynchronous, resilient communication between services.
-   **Circuit Breaker**: Prevents cascading failures in service-to-service communication.

---
## Local Setup

### Prerequisites
JDK 17+, Maven, a running SQL database, MongoDB, and Apache Kafka.

### Configuration
Update the `application.properties` in each service with your database credentials.

### Run Services
1.  Start Kafka, your database, and MongoDB.
2.  Run the services using `mvn spring-boot:run` in the following order:
    1.  `service-registry`
    2.  `api-gateway`
    3.  All other business services (`user-service`, `order-service`, etc.)

---
## Key API Endpoints

All requests go through the API Gateway (default port `8080`).

| Method | Endpoint                             | Description                         | Auth Required |
|:-------| :---                                 |:------------------------------------|:--------------|
| `POST` | `/api/v1/auth/register`              | Register a new user.                | No            |
| `POST` | `/api/v1/auth/login`                 | Log in to receive a JWT.            | No            |
| `POST` | `/api/v1/orders`                     | Place a new order.                  | Yes           |
| `GET`  | `/api/v1/orders`                     | Get details of all placed orders.   | Yes           |
| `GET`  | `/api/v1/orders/{id}`                | Get details for a specific order.   | Yes           |
| `GET`  | `/api/v1/products`                   | Get a list of all products.         | Yes           |
| `GET`  | `/api/v1/products/search?filter=...` | Search products on various filters. | Yes           |