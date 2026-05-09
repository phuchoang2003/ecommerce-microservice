# PROD-005: Delete Category

## User Story
As an **admin**, I want to delete categories, so that I can remove outdated or unnecessary categories.

## API
```
DELETE /api/v1/admin/categories/{categoryId}
```

### Headers
| Header | Value | Required |
|--------|-------|----------|
| Authorization | Bearer {token} | Yes |

### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| categoryId | UUID | Yes | Category unique identifier |

### Response (204 No Content)
Category successfully deleted.

### Error Responses
- **403 Forbidden**: User is not admin
- **404 Not Found**: Category does not exist
- **409 Conflict**: Category has products assigned

## Acceptance Criteria
- [ ] Only admin users can delete categories
- [ ] Returns 404 if category does not exist
- [ ] Returns 409 Conflict if any products are assigned to this category
- [ ] Also deletes child categories recursively
- [ ] Returns 204 on successful deletion
