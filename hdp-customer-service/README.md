# Customer Service

**Port:** 8082
**Database:** PostgreSQL (`customer_db` on host port 5435)
**Service Registry:** Eureka
**Config:** Spring Cloud Config Server

User profile management microservice. Owns the `users` and `user_addresses` tables. No event publishing, no external API calls — purely CRUD over the user profile boundary.

## API

| Method | Path | Description | Status |
|--------|------|-------------|--------|
| `POST` | `/v1/users` | Create a new user | 201 |
| `PUT`  | `/v1/users/{id}` | Full replace of a user profile | 200 |
| `DELETE` | `/v1/users/{id}` | Soft-delete a user (sets `deleted_at`) | 204 |

### Error codes

| Code | HTTP | When |
|------|------|------|
| `USER_FIELD_REQUIRED` | 400 | Required field blank (e.g. `fullName`, `email`) |
| `USER_FIELD_TOO_LONG` | 400 | Field exceeds max length |
| `ADDRESS_FIELD_REQUIRED` | 400 | Required address field blank |
| `jakarta.validation.*` | 400 | DTO-level validation failed (length/email/past) |
| `USER_NOT_FOUND` | 404 | User id not found |
| `USER_ALREADY_DELETED` | 409 | Operating on a soft-deleted user |
| `DUPLICATE_KEY` | 409 | Email already used by another user |

All errors come back as RFC 7807 `application/problem+json` with `traceId` and `errorCode` properties (handled by `GlobalExceptionHandler` from `hdp-library`).

### Example: create

```bash
curl -X POST http://localhost:8082/v1/users \
  -H 'Content-Type: application/json' \
  -d '{
    "fullName": "Nguyen Van A",
    "email": "a@example.com",
    "phone": "+84123456789",
    "dateOfBirth": "1990-01-01",
    "gender": "MALE",
    "avatarUrl": "https://cdn.example.com/a.png",
    "addresses": [
      { "street": "123 Le Loi", "city": "HCMC", "country": "VN" }
    ]
  }'
```

## Data Model

### `users`
| Column | Type | Notes |
|--------|------|-------|
| `id` | UUID PK | UUIDv7 |
| `full_name` | VARCHAR(255) | required |
| `email` | VARCHAR(255) | required, **unique** |
| `phone` | VARCHAR(20) | optional |
| `date_of_birth` | DATE | optional, must be in the past |
| `gender` | VARCHAR(10) | optional, `MALE` / `FEMALE` / `OTHER` |
| `avatar_url` | VARCHAR(500) | optional |
| `deleted_at` | TIMESTAMPTZ | NULL = active, set = soft-deleted |
| `created_at` | TIMESTAMPTZ | auto |
| `updated_at` | TIMESTAMPTZ | auto, bumped by trigger |

### `user_addresses`
| Column | Type | Notes |
|--------|------|-------|
| `id` | UUID PK | UUIDv7 |
| `user_id` | UUID FK → `users(id)` | `ON DELETE CASCADE` |
| `street` | VARCHAR(500) | required |
| `ward` | VARCHAR(100) | optional |
| `district` | VARCHAR(100) | optional |
| `city` | VARCHAR(100) | required |
| `country` | VARCHAR(100) | required |

## Architecture

Clean architecture, same layout as `hdp-order-service`:

```
com.hdp.customer_service
├── application/
│   ├── handler/      ← createuser / updateuser / deleteuser (impls)
│   └── port/in/      ← *Command, *Result, *Handler (interfaces)
│   └── port/out/     ← UserPersistencePort
├── domain/
│   ├── model/        ← User (AggregateRoot<UserId>)
│   ├── valueobject/  ← UserId, Gender, Address
│   └── exception/    ← UserErrorCode
└── infrastructure/
    └── adapter/
        ├── inbound/web/   ← UserController, DTOs, UserWebMapper
        └── outbound/persistence/jpa/   ← UserJpa, UserAddressJpa, UserRepositoryJpa, UserMapper, UserPersistenceAdapter
```

### Key design choices

- **No password / auth fields** in scope. This service is a profile store; auth is handled elsewhere.
- **Email is unique** at the DB level (`UNIQUE` constraint) and at the application level (`existsByEmail` check before insert/update).
- **Soft delete via `deleted_at` timestamp**. The row stays in the table; `findByIdAndDeletedAtIsNull` filters it out for future reads. Hard delete is intentionally out of scope.
- **PUT = full replace**. The request body must contain every field; the address list is replaced atomically (delete + insert via JPA `orphanRemoval`).
- **No domain events published.** Order / product services that need to react to user changes must subscribe to whatever integration mechanism gets added later (out of scope for v1).
- **`User` does not extend `BaseEntityJpa`** — that base expects an `is_deleted` Boolean column; the customer service uses a `deleted_at` timestamp instead. Timestamps and UUID generation are wired directly on `UserJpa`.

## Build

```bash
# 1. Publish shared library to mavenLocal (required)
./gradlew :hdp-library:publishToMavenLocal

# 2. Build the service
./gradlew :hdp-customer-service:build

# 3. Run tests (40 unit tests, ~2s, no DB required)
./gradlew :hdp-customer-service:test
```

## Run

### Option A — local dev with config server

```bash
# Start config server + customer-db + eureka via docker-compose
docker compose up -d config-server customer-db eureka-server

# Add the file `hdp-customer-service/docs/hdp-customer-service.yml` to your
# config repo (github.com/phuchoang2003/ecommerce-config-repo) under
# services/hdp-customer-service.yml, then run:
./gradlew :hdp-customer-service:bootRun
```

### Option B — local dev without config server

Set `SPRING_CONFIG_IMPORT=` (empty) to skip config server and rely on local `application.yml` only. The service will fail at startup because no datasource is configured — for a true standalone run you'd need to inline datasource/JPA/flyway config into `application.yml` or a profile file.

### Option C — docker-compose

```bash
docker compose up -d customer-db    # only the DB
docker compose up customer-service  # build + run the service
```

The compose file already sets `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` to talk to `customer-db` over the internal docker network.

## Smoke test (after `bootRun`)

```bash
# Health
curl http://localhost:8082/actuator/health

# Create
curl -i -X POST http://localhost:8082/v1/users \
  -H 'Content-Type: application/json' \
  -d '{"fullName":"Alice","email":"alice@example.com","addresses":[{"street":"1 Le Loi","city":"HCMC","country":"VN"}]}'

# Update
curl -i -X PUT http://localhost:8082/v1/users/<id> \
  -H 'Content-Type: application/json' \
  -d '{"fullName":"Alice Updated","email":"alice@example.com","addresses":[]}'

# Delete
curl -i -X DELETE http://localhost:8082/v1/users/<id>

# OpenAPI docs
open http://localhost:8082/swagger-ui.html
```
