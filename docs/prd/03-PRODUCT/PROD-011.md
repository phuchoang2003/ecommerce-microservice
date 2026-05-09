# PROD-011: Get Product

## User Story
As a **customer**, I want to view product details, so that I can make purchase decisions.

## API
```
GET /api/v1/products/{productId}
```

### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| productId | UUID | Yes | Product unique identifier |

### Response (200 OK)
```json
{
  "success": true,
  "data": {
    "id": "uuid-1",
    "name": "iPhone 15 Pro",
    "description": "Latest Apple smartphone",
    "price": 999.99,
    "images": ["url1.jpg", "url2.jpg"],
    "status": "ACTIVE",
    "sellerId": "uuid-seller",
    "categoryId": "uuid-category",
    "viewCount": 1501,
    "soldCount": 50,
    "variants": [
      {
        "id": "uuid-variant",
        "sku": "IPHONE-15-PRO-256",
        "options": {
          "color": "Black",
          "storage": "256GB"
        },
        "price": 999.99,
        "stock": 100
      }
    ],
    "createdAt": "2026-01-15T10:30:00Z",
    "updatedAt": "2026-04-01T08:00:00Z"
  }
}
```

### Error Responses
- **404 Not Found**: Product does not exist
- **403 Forbidden**: Product is not ACTIVE (for customers)

## Acceptance Criteria
- [ ] Returns full product details including variants
- [ ] Increments viewCount by 1 on each successful request
- [ ] Returns 404 if product does not exist
- [ ] Non-active products are only visible to the owning seller or admin
- [ ] No authentication required for active products
