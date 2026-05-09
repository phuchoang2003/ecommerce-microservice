# SELL-030: Sales Analytics

## User Story

As a seller, I want to view my sales analytics so that I can understand my business performance.

## API

**Endpoint:** `GET /api/v1/seller/analytics/sales`
**Authentication:** Required (Bearer Token - Seller Role)

### Query Parameters

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| period | String | No | 7d | Period: 7d, 30d, 90d, 1y |
| startDate | String | No | - | Custom start date (ISO 8601) |
| endDate | String | No | - | Custom end date (ISO 8601) |

### Response (200 OK)

```json
{
  "period": {
    "start": "2026-03-28T00:00:00Z",
    "end": "2026-04-04T23:59:59Z"
  },
  "summary": {
    "totalRevenue": 125000.00,
    "totalOrders": 156,
    "averageOrderValue": 801.28,
    "growthRate": 12.5
  },
  "dailySales": [
    {
      "date": "2026-03-28",
      "revenue": 18000.00,
      "orders": 22
    },
    {
      "date": "2026-03-29",
      "revenue": 15500.00,
      "orders": 18
    }
  ],
  "topProducts": [
    {
      "productId": "abc123",
      "name": "Wireless Headphones",
      "quantitySold": 45,
      "revenue": 6750.00
    }
  ]
}
```

### Error Responses

- `400 Bad Request` - Invalid date format or range
- `401 Unauthorized` - Not authenticated
- `403 Forbidden` - User does not have permission
- `404 Not Found` - No shop found for user

## Acceptance Criteria

- [ ] Seller can view sales analytics for own shop
- [ ] Data is filtered by selected time period
- [ ] Growth rate compares to previous period
- [ ] Daily breakdown is provided
- [ ] Top selling products are listed
- [ ] Only OWNER or MANAGER can access analytics