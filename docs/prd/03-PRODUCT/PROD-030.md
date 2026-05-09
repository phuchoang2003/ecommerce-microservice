# PROD-030: Admin List Products

## User Story
As an **admin**, I want to view all products across all sellers, so that I can monitor platform activity.

## API
```
GET /api/v1/admin/products
```

### Headers
| Header | Value | Required |
|--------|-------|----------|
| Authorization | Bearer {token} | Yes |

### Query Parameters
| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| page | Integer | No | 1 | Page number |
| size | Integer | No | 20 | Items per page (max 100) |
| status | String | No | - | Filter by status |
| sellerId | UUID | No | - | Filter by seller |
| categoryId | UUID | No | - | Filter by category |
| sortBy | String | No | createdAt | Sort field |
| sortOrder | String | No | desc | Sort order |

### Response (200 OK)
```json
{
  "success": true,
  "data": {
    "items": [
      {
        "id": "uuid-1",
        "name": "iPhone 15 Pro",
        "price": 999.99,
        "status": "ACTIVE",
        "sellerId": "uuid-seller",
        "categoryId": "uuid-category",
        "viewCount": 1500,
        "soldCount": 50,
        "createdAt": "2026-01-15T10:30:00Z",
        "updatedAt": "2026-04-01T08:00:00Z"
      }
    ],
    "pagination": {
      "page": 1,
      "size": 20,
      "totalItems": 5000,
      "totalPages": 250
    }
  }
}
```

### Error Responses
- **401 Unauthorized**: Not authenticated
- **403 Forbidden**: User is not admin

## Acceptance Criteria
- [ ] Only admin users can access this endpoint
- [ ] Returns products across all sellers
- [ ] Supports all status values (DRAFT, ACTIVE, INACTIVE, DELETED)
- [ ] Supports all filtering and pagination options
- [ ] Returns 403 if user is not admin
