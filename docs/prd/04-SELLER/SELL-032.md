# SELL-032: Rating Analytics

## User Story

As a seller, I want to view my rating analytics so that I can understand customer satisfaction.

## API

**Endpoint:** `GET /api/v1/seller/analytics/ratings`
**Authentication:** Required (Bearer Token - Seller Role)

### Query Parameters

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| period | String | No | 30d | Period: 30d, 90d, 1y |

### Response (200 OK)

```json
{
  "period": {
    "start": "2026-03-05T00:00:00Z",
    "end": "2026-04-04T23:59:59Z"
  },
  "summary": {
    "averageRating": 4.3,
    "totalReviews": 89,
    "responseTime": 2.5,
    "ratingDistribution": {
      "5": 45,
      "4": 28,
      "3": 10,
      "2": 4,
      "1": 2
    }
  },
  "ratingBreakdown": {
    "responseTime": {
      "score": 4.5,
      "weight": 30
    },
    "accuracy": {
      "score": 4.2,
      "weight": 40
    },
    "shippingSpeed": {
      "score": 4.1,
      "weight": 30
    }
  },
  "recentReviews": [
    {
      "id": "review-123",
      "productName": "Wireless Headphones",
      "rating": 5,
      "comment": "Great product, fast delivery!",
      "createdAt": "2026-04-03T15:30:00Z"
    }
  ]
}
```

### Rating Calculation Weights

| Factor | Weight |
|--------|--------|
| Accuracy | 40% |
| Response Time | 30% |
| Shipping Speed | 30% |

### Error Responses

- `400 Bad Request` - Invalid period
- `401 Unauthorized` - Not authenticated
- `403 Forbidden` - User does not have permission
- `404 Not Found` - No shop found for user

## Acceptance Criteria

- [ ] Seller can view rating analytics for own shop
- [ ] Overall rating and distribution shown
- [ ] Breakdown by component (response time, accuracy, shipping)
- [ ] Recent reviews are listed
- [ ] Response time average is calculated
- [ ] Rating below 3.0 triggers warning
- [ ] Only OWNER or MANAGER can access