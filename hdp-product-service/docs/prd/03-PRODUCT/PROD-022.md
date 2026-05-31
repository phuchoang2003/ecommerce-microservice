# PROD-022: Bulk Update Inventory

## User Story
As a **seller**, I want to update stock for multiple variants at once, so that I can manage inventory efficiently.

## API
```
POST /api/v1/seller/inventory/bulk
```

### Headers
| Header | Value | Required |
|--------|-------|----------|
| Authorization | Bearer {token} | Yes |

### Request Body
```json
{
  "updates": [
    {
      "variantId": "uuid-variant-1",
      "stock": 150
    },
    {
      "variantId": "uuid-variant-2",
      "stock": 200
    }
  ]
}
```

### Response (200 OK)
```json
{
  "success": true,
  "data": {
    "updated": [
      {
        "variantId": "uuid-variant-1",
        "stock": 150,
        "availableStock": 145
      },
      {
        "variantId": "uuid-variant-2",
        "stock": 200,
        "availableStock": 195
      }
    ],
    "failed": []
  }
}
```

### Error Responses
- **400 Bad Request**: Invalid input
- **401 Unauthorized**: Not authenticated
- **403 Forbidden**: Not authorized for one or more variants

## Acceptance Criteria
- [ ] Updates multiple variants in a single request
- [ ] Returns detailed results for each update
- [ ] Partial success supported (some variants may fail)
- [ ] Only updates variants belonging to the seller
- [ ] All validation rules from PROD-021 apply to each variant
