# E-Commerce Platform - Project Overview

## Services

| # | Service | Port | Database | Description |
|---|---------|------|----------|-------------|
| 1 | `gateway` | 8091 | - | API Gateway, routing, JWT validation |
| 2 | `user` | 8081 | MySQL | Authentication, user accounts, addresses |
| 3 | `product` | 8082 | PostgreSQL | Catalog, categories, inventory |
| 4 | `seller` | 8086 | PostgreSQL | Shop management, KYC, analytics |
| 5 | `cart` | 8083 | Redis+MySQL | Shopping cart |
| 6 | `order` | 8084 | PostgreSQL | Order processing, fulfillment |
| 7 | `payment` | 8085 | PostgreSQL | Stripe payments, escrow |
| 8 | `review` | 8088 | PostgreSQL | Ratings, reviews |
| 9 | `promotion` | 8087 | Redis+MySQL | Flash sales, coupons |
| 10 | `notification` | 8089 | MySQL | Multi-channel notifications |
| 11 | `search` | 8090 | Elasticsearch | Product search |
| 12 | `logistics` | 8092 | PostgreSQL | Shipping, tracking |
| 13 | `dispute` | 8093 | PostgreSQL | Dispute resolution |

---

## Kafka Topics

| Topic | Producers | Consumers |
|-------|-----------|-----------|
| `order-events` | order-service | payment, notification, logistics |
| `payment-events` | payment-service | order, notification |
| `product-events` | product-service | search, cart, notification |
| `user-events` | user-service | notification |
| `seller-events` | seller-service | notification |
| `review-events` | review-service | product, notification |
| `notification-events` | all services | notification-service |

---

## User Roles

| Role | Description |
|------|-------------|
| **GUEST** | Browse, search (no auth required) |
| **BUYER** | Purchase, review, track orders |
| **SELLER** | Manage shop, products, process orders |
| **ADMIN** | Platform management, disputes, categories |

---

## Order Status Flow

```
PENDING → PAID → PROCESSING → SHIPPED → DELIVERED → COMPLETED
    ↓         ↓
CANCELLED   PAYMENT_FAILED
```

---

## Quick Links

- [Gateway Service](./01-GATEWAY/index.md)
- [User Service](./02-USER/index.md)
- [Product Service](./03-PRODUCT/index.md)
- [Seller Service](./04-SELLER/index.md)
- [Cart Service](./05-CART/index.md)
- [Order Service](./06-ORDER/index.md)
- [Payment Service](./07-PAYMENT/index.md)
- [Review Service](./08-REVIEW/index.md)
- [Promotion Service](./09-PROMOTION/index.md)
- [Notification Service](./10-NOTIFICATION/index.md)
- [Search Service](./11-SEARCH/index.md)
- [Logistics Service](./12-LOGISTICS/index.md)
- [Dispute Service](./13-DISPUTE/index.md)
