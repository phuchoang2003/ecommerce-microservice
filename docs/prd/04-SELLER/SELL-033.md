# SELL-033: Overview Dashboard

## User Story

As a seller, I want to view an overview dashboard of my shop's performance so that I can quickly assess business health.

## API

**Endpoint:** `GET /api/v1/seller/analytics/overview`
**Authentication:** Required (Bearer Token - Seller Role)

### Query Parameters

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| period | String | No | 7d | Period: 7d, 30d, 90d |

### Response (200 OK)

```json
{
  "shopStatus": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Acme Electronics",
    "status": "ACTIVE",
    "rating": 4.3
  },
  "sales": {
    "revenue": 125000.00,
    "orders": 156,
    "growthRate": 12.5
  },
  "traffic": {
    "visitors": 5420,
    "conversionRate": 2.9,
    "growthRate": 8.3
  },
  "ratings": {
    "averageRating": 4.3,
    "totalReviews": 89,
    "responseTimeHours": 2.5
  },
  "alerts": [
    {
      "type": "WARNING",
      "msg": "Average rating dropped below 4.0",
      "code": "LOW_RATING"
    },
    {
      "type": "INFO",
      "msg": "3 pending orders require attention",
      "code": "PENDING_ORDERS"
    }
  ],
  "quickStats": {
    "productsActive": 145,
    "productsOutOfStock": 12,
    "pendingOrders": 3,
    "shippedToday": 18
  }
}
```

### Alert Types

| Type | Description |
|------|-------------|
| WARNING | Requires attention (low rating, low stock) |
| INFO | Informational (pending orders, etc.) |
| CRITICAL | Urgent (shop suspension warning) |

### Error Responses

- `400 Bad Request` - Invalid period
- `401 Unauthorized` - Not authenticated
- `403 Forbidden` - User does not have permission
- `404 Not Found` - No shop found for user

## Acceptance Criteria

- [ ] Seller can view comprehensive dashboard
- [ ] All key metrics are summarized
- [ ] Alerts highlight issues requiring attention
- [ ] Growth rates compare to previous period
- [ ] Quick stats show operational metrics
- [ ] Shop suspension warning if rating below 3.0 for 30 days
- [ ] Only OWNER or MANAGER can access