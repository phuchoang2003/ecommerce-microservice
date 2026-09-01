# E-Commerce Microservice Platform

> **Status: 🚧 Under Construction** — This project is actively being developed.

---

## Project Overview

An **e-commerce microservice platform** built with Spring Boot, Apache Kafka, and PostgreSQL — designed to demonstrate modern microservices architecture patterns including:

- **Clean Architecture** with clear separation of concerns
- **Domain-Driven Design (DDD)** with aggregate roots and domain events
- **Event-Driven Communication** via Apache Kafka with Avro schema serialization
- **CQRS Pattern** ready — separate read/write models
- **Event Sourcing** support via `SpringDomainEventPublisher`

## Purpose

This platform serves as a **reference implementation** and **learning resource** for building production-grade microservices. It showcases:

1. **Multi-service architecture** — Order, Product, and Notification services
2. **Shared common libraries** — Reusable domain models, infrastructure components
3. **Async messaging** — Kafka-based event-driven communication
4. **Database per service** — Each service owns its data and schema
5. **API documentation** — OpenAPI/Swagger for all services

## Current Status

| Component | Status | Notes |
|-----------|--------|-------|
| Common Libraries | ✅ Done | Core, Web, Infrastructure, Persistence, Messaging, Test |
| Order Service | 🔨 Building | REST API, domain model, persistence |
| Product Service | 🔨 Building | REST API, domain model, persistence |
| Notification Service | 🔨 Building | Kafka consumer → notification sender |
| API Gateway | ⏳ Planned | Central entry point |
| Authentication | ⏳ Planned | JWT-based auth |
| Inventory Service | ⏳ Planned | Stock management |

---

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                         CLIENTS                                   │
│              (Web App, Mobile, REST API Consumers)                │
└─────────────────────────┬───────────────────────────────────────┘
                          │ HTTP/REST
                          ▼
┌─────────────────────────────────────────────────────────────────┐
│                      API GATEWAY (Future)                        │
└─────────────────────────┬───────────────────────────────────────┘

         ┌─────────────────────┼─────────────────────┐
         │                     │                     │
         ▼                     ▼                     ▼
┌─────────────────┐  ┌─────────────────┐  ┌─────────────────────┐
│  Order Service   │  │ Product Service │  │ Notification Service │
│    (Port 8080)   │  │   (Port 8082)   │  │    (Kafka Consumer)  │
└────────┬────────┘  └────────┬────────┘  └──────────┬──────────┘
         │                    │                      │
         │  PostgreSQL        │  PostgreSQL          │
         ▼                    ▼                      ▼
    ┌─────────┐          ┌─────────┐          ┌─────────┐
    │ order_db│          │product_db│          │   Kafka  │
    │ (5433)  │          │ (5434)   │          │  Topics  │
    └─────────┘          └─────────┘          └─────────┘
                              │
                         ┌────┴────┐
                         │ Kafka   │
                         │ (9092)  │
                         └─────────┘
```

---

## Project Structure

```
ecommerce-microservice/
├── hdp-library/              # Shared library: domain primitives + web + infrastructure + observability
│
├── hdp-order-service/         # Order management microservice (owns its persistence + Kafka/Avro inline)
├── hdp-product-service/       # Product management microservice (owns its persistence + filestorage + Kafka/Avro inline)
└── hdp-notification-service/  # Kafka consumer → notification sender
```

---

## Key Design Patterns

### Clean Architecture Layers
```
application/port/in/     ← Usecase interfaces (DTO @Valid handles null/length; domain invariants live in value objects)
application/port/out/    ← Persistence port interfaces
application/event/       ← Domain event handlers
domain/model/             ← Aggregate roots
infrastructure/adapter/inbound/web/    ← Controllers, DTOs, filters
infrastructure/adapter/outbound/       ← Repository implementations, event publishers
```

### Usecase Pattern
```java
public interface Usecase<I, O> {
    O execute(I input);
}
```

### Domain Events with Event Sourcing
```java
// Entity collects events
public class Order extends AggregateRoot<OrderId> {
    public void place() {
        addDomainEvent(new OrderPlacedEvent(this.id, ...));
    }
}

// Events published after transaction commits via SpringDomainEventPublisher
// Handlers extend AbstractDomainEventHandler<T>
```

---

## Quick Start

### Prerequisites
- Java 25+
- Docker & Docker Compose
- Gradle

### 1. Start Infrastructure

```bash
docker compose up -d
```

| Service | Port | Description |
|---------|------|-------------|
| PostgreSQL (Orders) | 5433 | Order service database |
| PostgreSQL (Products) | 5434 | Product service database |
| Apache Kafka | 9092 | Messaging (KRaft mode, no Zookeeper) |
| Kafka UI | 8081 | Kafka management UI |
| Schema Registry | 8085 | Avro schema registry |

### 2. Publish Common Libraries

```bash
./gradlew :hdp-common:publishToMavenLocal
```

### 3. Build & Run

```bash
# Build all modules
./gradlew build

# Run services
./gradlew :hdp-order-service:bootRun
./gradlew :hdp-product-service:bootRun
./gradlew :hdp-notification-service:bootRun
```

---

## API Documentation

| Service | URL | Docs |
|---------|-----|------|
| Order Service | http://localhost:8080 | /swagger-ui.html |
| Product Service | http://localhost:8082 | /swagger-ui.html |
| Kafka UI | http://localhost:8081 | Web-based Kafka management |
| Schema Registry | http://localhost:8085 | Avro schema management |

---

## Technology Stack

| Component | Technology | Version |
|-----------|------------|---------|
| Runtime | Java | 25 |
| Framework | Spring Boot | 4.0.5 |
| Database | PostgreSQL | 18 |
| Messaging | Apache Kafka | 3.7.0 (KRaft) |
| Schema Registry | Confluent Schema Registry | 7.5.0 |
| API Docs | SpringDoc OpenAPI | 2.8.0 |
| Migration | Flyway | latest |
| Serialization | Avro | latest |

---

## Roadmap

| Feature | Status |
|---------|--------|
| Common Libraries (Core, Web, Infra, Persistence, Messaging, Test) | ✅ |
| Order Service (CRUD, Domain Events) | 🔨 |
| Product Service (CRUD, Domain Events) | 🔨 |
| Notification Service (Kafka Consumer) | 🔨 |
| Flyway Migrations | 🔨 |
| Unit Tests | 🔨 |
| API Gateway | ⏳ |
| JWT Authentication | ⏳ |
| Inventory Service | ⏳ |
| Payment Service | ⏳ |
| Full CI/CD Pipeline | ⏳ |

---

## Contributing

1. Create a feature branch
2. Follow Clean Architecture guidelines
3. Write tests for all new functionality
4. Ensure all tests pass: `./gradlew test`
5. Submit a pull request

---

## License

MIT License