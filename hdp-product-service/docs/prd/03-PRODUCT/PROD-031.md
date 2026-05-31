# PROD-031: Admin Update Product Status

## User Story
As an **admin**, I want to update any product status, so that I can manage platform content.

## API
```
PUT /api/v1/admin/products/{productId}/status
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
  "status": "INACTIVE",
  "reason": "Policy violation"
}
```

### Valid Status Values
- ACTIVE
- INACTIVE
- DELETED

### Response (200 OK)
```json
{
  "success": true,
  "data": {
    "id": "uuid-1",
    "status": "INACTIVE",
    "reason": "Policy violation",
    "updatedAt": "2026-04-04T14:00:00Z"
  }
}
```

### Error Responses
- **400 Bad Request**: Invalid status
- **401 Unauthorized**: Not authenticated
- **403 Forbidden**: User is not admin
- **404 Not Found**: Product does not exist

## Acceptance Criteria
- [ ] Admin can update status of any product
- [ ] Reason field is recorded for audit purposes
- [ ] No restrictions on status transitions (unlike seller)
- [ ] Returns 404 if product does not exist
- [ ] Returns 403 if user is not admin
