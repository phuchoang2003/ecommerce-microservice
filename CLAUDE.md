# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Quick Start

```bash
# 1. Publish common libraries to mavenLocal (required before building services)
./gradlew :hdp-common:publishToMavenLocal
./gradlew :hdp-common-test:publishToMavenLocal

# 2. Start infrastructure (PostgreSQL, Kafka, Schema Registry)
docker compose up -d

# 3. Build and run
./gradlew build
./gradlew :hdp-order-service:bootRun
```

## Build Commands

```bash
# Build all modules
./gradlew build

# Run tests
./gradlew test

# Run tests for a specific module
./gradlew :hdp-order-service:test
./gradlew :hdp-product-service:test

# Run a specific test class
./gradlew :hdp-order-service:test --tests "com.hdp.order_service.application.Usecase.CreateOrderUsecaseImplTest"

# Clean build
./gradlew clean build
```

## Architecture

### Module Structure

- **hdp-common-core** — Domain layer: `AggregateRoot`, `DomainEvent` base classes
- **hdp-common-web** — Web infrastructure: controllers, DTOs, filters, CORS, swagger
- **hdp-common-infrastructure** — Thread pools, schedulers, request context propagation
- **hdp-common-persistence** — JPA entities, repositories, Flyway migrations, UUIDv7 ID generator
- **hdp-common-messaging** — Kafka producers/consumers with Avro support
- **hdp-common-test** — Test utilities (`@ExpectMaxQueryCount`, `QueryCountExtension`)

- **hdp-order-service** — Order management microservice
- **hdp-product-service** — Product management microservice
- **hdp-notification-service** — Kafka consumer → notification sender

### Clean Architecture Layers (per module)

```
application/port/in/          ← Usecase interfaces, validation (Rule, ValidationResult)
application/port/out/         ← Persistence port interfaces
application/event/            ← Domain event handlers
domain/model/                 ← Aggregate roots
infrastructure/adapter/inbound/web/   ← Controllers, DTOs, filters
infrastructure/adapter/outbound/      ← Repository implementations, event publishers
```

### Key Patterns

**Usecase Pattern** — All business logic goes through `Usecase<I, O>` interface:
```java
public interface Usecase<I, O> {
    O execute(I input);
}
```

**Validation** — Use `Rule<T>` fluent builder with `ValidationResult`:
```java
ValidationResult result = new ValidationResult();
Rule.of(value, "field", result)
    .notNull("field is required")
    .greaterThan(0, "field must be positive")
    .throwIfInvalid();
```

**Domain Events** — Entities extend `AggregateRoot<ID>` and call `addDomainEvent(DomainEvent)` to collect events. Events are published via `SpringDomainEventPublisher` after the transaction commits. Handlers extend `AbstractDomainEventHandler<T>` overriding `doHandle(T event)` and optionally `beforeHandle`/`afterHandle`.

**RequestContextTaskDecorator** — Propagates trace IDs and request context to async/thread pool tasks.

## Infrastructure

| Service | Port | Notes |
|---------|------|-------|
| PostgreSQL (orders) | 5433 | Order service database |
| PostgreSQL (products) | 5434 | Product service database |
| Apache Kafka | 9092 | Messaging (KRaft mode) |
| Kafka UI | 8081 | Kafka management UI |
| Schema Registry | 8085 | Avro schema registry |
| OpenAPI Docs | /swagger-ui.html | API documentation |

- **Migrations**: Flyway for schema migrations
- **Messaging**: Apache Kafka 3.7.0 with KRaft (no Zookeeper)

## Gotchas

- **Publish common libs first** — Always run `./gradlew :hdp-common:publishToMavenLocal :hdp-common-test:publishToMavenLocal` before building services
- **KRaft mode ports** — Kafka uses ports 29092 (internal) and 9092 (external); docker-compose maps 9092→localhost
- **Non-standard DB ports** — Order service connects to 5433, product service to 5434 (not the default 5432)