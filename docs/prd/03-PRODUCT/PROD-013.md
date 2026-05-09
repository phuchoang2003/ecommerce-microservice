# PROD-013: Update Product

## User Story
As a **seller**, I want to update product information, so that I can keep my listings accurate.

## API
```
PUT /api/v1/seller/products/{productId}
```

### Headers
| Header | Value | Required |
|--------|-------|----------|
| Authorization | Bearer {token} | Yes |

### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| productId | UUID | Yes | Product unique identifier |

### Request Body
```json
{
  "name": "iPhone 15 Pro Max",
  "description": "Updated description",
  "categoryId": "uuid-new-category",
  "price": 1099.99
}
```

### Response (200 OK)
```json
{
  "success": true,
  "data": {
    "id": "uuid-1",
    "name": "iPhone 15 Pro Max",
    "description": "Updated description",
    "price": 1099.99,
    "status": "DRAFT",
    "updatedAt": "2026-04-04T14:00:00Z"
  }
}
```

### Error Responses
- **400 Bad Request**: Invalid input
- **401 Unauthorized**: Not authenticated
- **403 Forbidden**: Not the product owner
- **404 Not Found**: Product does not exist

## Acceptance Criteria
- [ ] Seller can only update their own products
- [ ] Cannot update status via this endpoint (use PROD-014)
- [ ] Cannot update images via this endpoint (use PROD-015)
- [ ] Returns 404 if product does not exist
- [ ] Returns 403 if seller does not own the product
