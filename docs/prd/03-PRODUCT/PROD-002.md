# PROD-002: Get Category

## User Story
As a **customer**, I want to get details of a specific category, so that I can understand what products are in that category.

## API
```
GET /api/v1/categories/{categoryId}
```

### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| categoryId | UUID | Yes | Category unique identifier |

### Response (200 OK)
```json
{
  "success": true,
  "data": {
    "id": "uuid-1",
    "name": "Smartphones",
    "parentId": "uuid-parent",
    "level": 2,
    "createdAt": "2026-01-15T10:30:00Z"
  }
}
```

### Error Responses
- **404 Not Found**: Category does not exist

## Acceptance Criteria
- [ ] Returns category details including parent reference
- [ ] Returns 404 if category does not exist
- [ ] No authentication required
