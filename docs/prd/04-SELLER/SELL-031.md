# SELL-031: Traffic Analytics

## User Story

As a seller, I want to view my shop's traffic analytics so that I can understand visitor patterns.

## API

**Endpoint:** `GET /api/v1/seller/analytics/traffic`
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
    "totalVisitors": 5420,
    "uniqueVisitors": 3200,
    "pageViews": 12500,
    "growthRate": 8.3
  },
  "dailyTraffic": [
    {
      "date": "2026-03-28",
      "visitors": 780,
      "pageViews": 1850
    },
    {
      "date": "2026-03-29",
      "visitors": 820,
      "pageViews": 1920
    }
  ],
  "trafficSources": {
    "direct": 35.5,
    "search": 42.3,
    "social": 15.2,
    "referral": 7.0
  },
  "topPages": [
    {
      "path": "/shop/550e8400/products",
      "views": 5200
    },
    {
      "path": "/shop/550e8400",
      "views": 3100
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

- [ ] Seller can view traffic analytics for own shop
- [ ] Data is filtered by selected time period
- [ ] Traffic sources breakdown is provided
- [ ] Daily visitor trends are charted
- [ ] Top viewed pages are listed
- [ ] Unique visitors vs total visits distinguished
- [ ] Only OWNER or MANAGER can access