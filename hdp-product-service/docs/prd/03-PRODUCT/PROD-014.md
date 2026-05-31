# PROD-014: Update Product Status

## User Story
As a **seller**, I want to update my product status, so that I can activate or deactivate listings.

## API
```
PUT /api/v1/seller/products/{productId}/status
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
  "status": "ACTIVE"
}
```

### Valid Status Transitions
- DRAFT -> ACTIVE (if product has at least one variant with stock > 0)
- DRAFT -> DELETED
- ACTIVE -> INACTIVE
- ACTIVE -> DELETED
- INACTIVE -> ACTIVE
- INACTIVE -> DELETED

### Response (200 OK)
```json
{
  "success": true,
  "data": {
    "id": "uuid-1",
    "status": "ACTIVE",
    "updatedAt": "2026-04-04T14:00:00Z"
  }
}
```

### Error Responses
- **400 Bad Request**: Invalid status transition
- **401 Unauthorized**: Not authenticated
- **403 Forbidden**: Not the product owner
- **404 Not Found**: Product does not exist

## Acceptance Criteria
- [ ] Seller can only update status of their own products
- [ ] Cannot activate product without at least one variant with stock > 0
- [ ] Returns 400 for invalid status transitions
- [ ] Returns 404 if product does not exist
