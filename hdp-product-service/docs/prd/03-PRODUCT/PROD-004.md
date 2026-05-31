# PROD-004: Update Category

## User Story
As an **admin**, I want to update category information, so that I can correct or modify category details.

## API
```
PUT /api/v1/admin/categories/{categoryId}
```

### Headers
| Header | Value | Required |
|--------|-------|----------|
| Authorization | Bearer {token} | Yes |

### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| categoryId | UUID | Yes | Category unique identifier |

### Request Body
```json
{
  "name": "Updated Category Name"
}
```

### Response (200 OK)
```json
{
  "success": true,
  "data": {
    "id": "uuid-1",
    "name": "Updated Category Name",
    "parentId": "uuid-parent",
    "level": 2,
    "updatedAt": "2026-04-04T14:00:00Z"
  }
}
```

### Error Responses
- **400 Bad Request**: Invalid input
- **403 Forbidden**: User is not admin
- **404 Not Found**: Category does not exist

## Acceptance Criteria
- [ ] Only admin users can update categories
- [ ] Cannot change parentId (category hierarchy is immutable after creation)
- [ ] Category name must be unique at the same level
- [ ] Returns 404 if category does not exist
