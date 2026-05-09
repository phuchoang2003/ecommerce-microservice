# PROD-003: Create Category

## User Story
As an **admin**, I want to create new categories, so that the product catalog can be organized.

## API
```
POST /api/v1/admin/categories
```

### Headers
| Header | Value | Required |
|--------|-------|----------|
| Authorization | Bearer {token} | Yes |

### Request Body
```json
{
  "name": "Android Phones",
  "parentId": "uuid-parent-category"
}
```

### Validation Rules
- `name`: Required, max 100 characters
- `parentId`: Optional (null for L1 categories)

### Response (201 Created)
```json
{
  "success": true,
  "data": {
    "id": "uuid-new",
    "name": "Android Phones",
    "parentId": "uuid-parent-category",
    "level": 3,
    "createdAt": "2026-04-04T12:00:00Z"
  }
}
```

### Error Responses
- **400 Bad Request**: Invalid input
- **403 Forbidden**: User is not admin
- **404 Not Found**: Parent category does not exist

## Acceptance Criteria
- [ ] Only admin users can create categories
- [ ] Category level is automatically set based on parent (L1=1, L2=2, L3=3)
- [ ] Maximum 3 levels enforced (cannot create L4 or deeper)
- [ ] Category names must be unique at the same level
