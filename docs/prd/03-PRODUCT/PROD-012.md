# PROD-012: Create Product

## User Story
As a **seller**, I want to create new products, so that I can sell items on the platform.

## API
```
POST /api/v1/seller/products
```

### Headers
| Header | Value | Required |
|--------|-------|----------|
| Authorization | Bearer {token} | Yes |

### Request Body
```json
{
  "name": "iPhone 15 Pro",
  "description": "Latest Apple smartphone with A17 Pro chip",
  "categoryId": "uuid-category",
  "price": 999.99,
  "variants": [
    {
      "sku": "IPHONE-15-PRO-256",
      "options": {
        "color": "Black",
        "storage": "256GB"
      },
      "price": 999.99,
      "stock": 100
    }
  ]
}
```

### Validation Rules
- `name`: Required, max 200 characters
- `description`: Optional, max 5000 characters
- `categoryId`: Required, must exist
- `price`: Required, must be >= 0
- `variants`: Optional array, each variant needs sku, options, price, stock

### Response (201 Created)
```json
{
  "success": true,
  "data": {
    "id": "uuid-new",
    "name": "iPhone 15 Pro",
    "description": "Latest Apple smartphone with A17 Pro chip",
    "price": 999.99,
    "images": [],
    "status": "DRAFT",
    "sellerId": "uuid-seller",
    "categoryId": "uuid-category",
    "viewCount": 0,
    "soldCount": 0,
    "variants": [...],
    "createdAt": "2026-04-04T12:00:00Z",
    "updatedAt": "2026-04-04T12:00:00Z"
  }
}
```

### Error Responses
- **400 Bad Request**: Invalid input
- **401 Unauthorized**: Not authenticated
- **403 Forbidden**: Not a seller
- **404 Not Found**: Category does not exist

## Acceptance Criteria
- [ ] Product is created with status DRAFT
- [ ] Seller can only create products for themselves
- [ ] At least one variant is required (if variants provided)
- [ ] SKU must be unique per seller
- [ ] Product is not visible to customers until status is ACTIVE
