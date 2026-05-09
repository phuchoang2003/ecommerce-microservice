# CART-026: Validate Single Item

## User Story

As a **logged-in customer**, I want to **validate a single item in my cart** so that I can **check if a specific item is still available without validating the entire cart**.

## API

```
POST /api/v1/cart/items/validate
Authorization: Bearer {token}
Content-Type: application/json

{
  "productId": "prod-uuid-001",
  "variantId": "var-uuid-001",
  "quantity": 2
}
```

### Response Example

```json
{
  "valid": true,
  "productId": "prod-uuid-001",
  "variantId": "var-uuid-001",
  "requestedQuantity": 2,
  "availableQuantity": 50,
  "currentPrice": 29.99,
  "msg": "Item is available"
}
```

### Invalid Response Example

```json
{
  "valid": false,
  "productId": "prod-uuid-001",
  "variantId": "var-uuid-001",
  "requestedQuantity": 100,
  "availableQuantity": 50,
  "currentPrice": 29.99,
  "reason": "INSUFFICIENT_STOCK",
  "msg": "Only 50 items available"
}
```

## Acceptance Criteria

- [ ] Validates the specified item
- [ ] Checks product and variant existence
- [ ] Verifies stock availability
- [ ] Returns current price
- [ ] Returns 400 if productId, variantId, or quantity is missing/invalid
- [ ] Returns 401 if not authenticated
- [ ] Indicates if requested quantity exceeds available stock
