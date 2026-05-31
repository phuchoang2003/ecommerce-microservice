# PROD-020: View Inventory

## User Story
As a **seller**, I want to view inventory for my products, so that I can monitor stock levels.

## API
```
GET /api/v1/seller/products/{productId}/inventory
```

### Headers
| Header | Value | Required |
|--------|-------|----------|
| Authorization | Bearer {token} | Yes |

### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| productId | UUID | Yes | Product unique identifier |

### Response (200 OK)
```json
{
  "success": true,
  "data": {
    "productId": "uuid-1",
    "productName": "iPhone 15 Pro",
    "variants": [
      {
        "id": "uuid-variant",
        "sku": "IPHONE-15-PRO-256",
        "options": {
          "color": "Black",
          "storage": "256GB"
        },
        "price": 999.99,
        "stock": 100,
        "reservedStock": 5,
        "availableStock": 95
      }
    ],
    "totalStock": 100,
    "totalReservedStock": 5,
    "totalAvailableStock": 95
  }
}
```

### Error Responses
- **401 Unauthorized**: Not authenticated
- **403 Forbidden**: Not the product owner
- **404 Not Found**: Product does not exist

## Acceptance Criteria
- [ ] Seller can only view inventory of their own products
- [ ] Returns all variants with stock information
- [ ] Calculates availableStock = stock - reservedStock
- [ ] Returns 404 if product does not exist
- [ ] Returns 403 if seller does not own the product
