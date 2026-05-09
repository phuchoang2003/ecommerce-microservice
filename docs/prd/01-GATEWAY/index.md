# Gateway Service

**Port:** 8091
**Technology:** Spring Cloud Gateway, Resilience4j

## Features

| File | Description |
|------|-------------|
| [routing.md](./routing.md) | API routing configuration, route table |
| [auth.md](./auth.md) | JWT validation filter, token forwarding |
| [rate-limiting.md](./rate-limiting.md) | Rate limiting rules per role |
| [discovery.md](./discovery.md) | Service discovery with Consul |

## Overview

The Gateway Service is the single entry point for all client requests. It handles:
- Route all requests to appropriate microservices
- Validate JWT tokens
- Rate limiting by role
- Service discovery via Consul
- Circuit breaker with Resilience4j
- Error handling (401, 403, 404, 429, 500)

## Architecture

```
Client Request
      │
      ▼
┌─────────────┐
│   Gateway   │ ──► JWT Validation
│   :8091     │ ──► Rate Limiting
│             │ ──► Circuit Breaker
└─────────────┘
      │
      ▼
┌──────────────────────────────────────┐
│         Service Discovery            │
│            (Consul)                 │
└──────────────────────────────────────┘
      │
      ▼
┌──────────┐ ┌──────────┐ ┌──────────┐
│  User    │ │ Product  │ │  Cart   │
│ Service  │ │ Service  │ │ Service  │
│  :8081   │ │  :8082   │ │  :8083  │
└──────────┘ └──────────┘ └──────────┘
```

## Route Summary

| Route | Target Service | Auth Required |
|-------|----------------|---------------|
| `/api/v1/users/**` | user-service:8081 | Yes |
| `/api/v1/products/**` | product-service:8082 | No (browse) |
| `/api/v1/cart/**` | cart-service:8083 | Yes |
| `/api/v1/orders/**` | order-service:8084 | Yes |
| `/api/v1/payments/**` | payment-service:8085 | Yes |
| `/api/v1/seller/**` | seller-service:8086 | Yes + SELLER role |
| `/api/v1/reviews/**` | review-service:8088 | No (browse) |
| `/api/v1/notifications/**` | notification-service:8089 | Yes |
| `/api/v1/search/**` | search-service:8090 | No |

## JWT Headers Forwarded

The gateway extracts user information from JWT and forwards these headers to downstream services:

| Header | Description |
|--------|-------------|
| `X-User-Id` | User's unique identifier |
| `X-User-Role` | User's role (GUEST, BUYER, SELLER, ADMIN) |
| `X-Seller-Id` | Seller's unique identifier (if SELLER role) |

## Error Responses

| Status | Meaning | Response Body |
|--------|---------|---------------|
| 401 | Unauthorized - Invalid/missing JWT | `{"error": "UNAUTHORIZED", "msg": "Invalid or expired token"}` |
| 403 | Forbidden - Insufficient role | `{"error": "FORBIDDEN", "msg": "Insufficient permissions"}` |
| 404 | Route not found | `{"error": "NOT_FOUND", "msg": "Endpoint not found"}` |
| 429 | Rate limit exceeded | `{"error": "RATE_LIMITED", "msg": "Too many requests", "retryAfter": 60}` |
| 500 | Internal server error | `{"error": "INTERNAL_ERROR", "msg": "Service temporarily unavailable"}` |
| 503 | Service unavailable (circuit open) | `{"error": "SERVICE_UNAVAILABLE", "msg": "Service temporarily unavailable"}` |

---

## Quick Links

- [Routing Configuration](./routing.md)
- [Authentication & JWT](./auth.md)
- [Rate Limiting](./rate-limiting.md)
- [Service Discovery](./discovery.md)
