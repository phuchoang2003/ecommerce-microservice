# PROD-021: Update Stock

## User Story
As a **seller**, I want to update stock for product variants, so that I can keep inventory accurate.

## API
```
PUT /api/v1/seller/inventory/{variantId}
```

### Headers
| Header | Value | Required |
|--------|-------|----------|
| Authorization | Bearer {token} | Yes |

### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| variantId | UUID | Yes | Product variant unique identifier |

### Request Body
```json
{
  "stock": 150
}
```

### Response (200 OK)
```json
{
  "success": true,
  "data": {
    "variantId": "uuid-variant",
    "sku": "IPHONE-15-PRO-256",
    "stock": 150,
    "reservedStock": 5,
    "availableStock": 145,
    "updatedAt": "2026-04-04T14:00:00Z"
  }
}
```

### Error Responses
- **400 Bad Request**: Stock cannot be negative
- **401 Unauthorized**: Not authenticated
- **403 Forbidden**: Not the variant owner
- **404 Not Found**: Variant does not exist

## Acceptance Criteria
- [ ] Seller can only update stock of variants belonging to their products
- [ ] Stock cannot be less than reservedStock
- [ ] New stock is validated against reserved quantities
- [ ] Returns 404 if variant does not exist
