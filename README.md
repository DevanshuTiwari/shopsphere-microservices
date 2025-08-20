# 🛍️ ShopSphere: A Microservices E-Commerce Platform

A complete e-commerce backend built with Java and Spring Boot, demonstrating a modern, event-driven microservices architecture using Spring Cloud, Eureka, and Apache Kafka.

---
## Technology Stack

-   **Backend**: Java 17+, Spring Boot 3.x, Spring Cloud, Spring Security
-   **Infrastructure**: Spring Cloud Gateway, Netflix Eureka (Service Discovery), Apache Kafka
-   **Data**: Spring Data JPA, Hibernate, MySQL/PostgreSQL
-   **API**: REST, WebClient
-   **Build**: Maven

---
## Services Included

-   `service-registry`: Eureka server for service discovery.
-   `api-gateway`: Single entry point for all client requests.
-   `user-service`: Handles user registration and JWT authentication.
-   `product-catalog-service`: Manages products and inventory.
-   `order-service`: Orchestrates the order placement workflow.
-   `payment-service`: Simulates payment processing.
-   `notification-service`: Sends asynchronous email notifications.

---
## Local Setup

1.  **Prerequisites**: JDK 17+, Maven, a running SQL database, and Apache Kafka.
2.  **Configuration**: Update the `application.properties` in each service with your database credentials.
3.  **Run Services**: Start Kafka, then run the services using `mvn spring-boot:run` in the following order:
    1. `service-registry`
    2. `api-gateway`
    3. All other business services (`user-service`, `order-service`, etc.)

---
## Key API Endpoints

All requests go through the API Gateway (default port `8080`).

-   `POST /api/v1/auth/register`: Register a new user.
-   `POST /api/v1/auth/login`: Log in to receive a JWT.
-   `POST /api/v1/orders`: Place a new order (requires authentication).
-   `POST /api/v1/products`: Get all the products (requires authentication).
-   `POST /api/v1/products/search`: Search products on various filters (requires authentication).
