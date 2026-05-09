# PROD-023: Low Stock Alert

## User Story
As a **seller**, I want to see products with low stock, so that I can reorder inventory in time.

## API
```
GET /api/v1/seller/inventory/low-stock?threshold=10
```

### Headers
| Header | Value | Required |
|--------|-------|----------|
| Authorization | Bearer {token} | Yes |

### Query Parameters
| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| threshold | Integer | No | 10 | Stock threshold value |

### Response (200 OK)
```json
{
  "success": true,
  "data": {
    "threshold": 10,
    "lowStockVariants": [
      {
        "productId": "uuid-1",
        "productName": "iPhone 15 Pro",
        "variantId": "uuid-variant",
        "sku": "IPHONE-15-PRO-256",
        "options": {
          "color": "Black",
          "storage": "256GB"
        },
        "stock": 5,
        "reservedStock": 2,
        "availableStock": 3
      }
    ],
    "totalLowStockVariants": 1
  }
}
```

### Error Responses
- **401 Unauthorized**: Not authenticated

## Acceptance Criteria
- [ ] Returns variants where availableStock <= threshold
- [ ] Threshold is configurable via query parameter
- [ ] Only returns variants belonging to the seller
- [ ] Results sorted by availableStock ascending (lowest first)
