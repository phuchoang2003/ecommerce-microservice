# CART-002: Add Item

## User Story

As a **logged-in customer**, I want to **add items to my cart** so that I can **collect products I want to purchase**.

## API

```
POST /api/v1/cart/items
Authorization: Bearer {token}
Content-Type: application/json

{
  "productId": "prod-uuid-001",
  "variantId": "var-uuid-001",
  "quantity": 1
}
```

### Response Example

```json
{
  "itemId": "item-uuid-22222",
  "productId": "prod-uuid-001",
  "variantId": "var-uuid-001",
  "productName": "Wireless Mouse",
  "variantName": "Black",
  "quantity": 1,
  "unitPrice": 29.99,
  "subtotal": 29.99,
  "selected": true,
  "addedAt": "2026-04-04T14:00:00Z"
}
```

## Acceptance Criteria

- [ ] Adds item to user's cart
- [ ] Creates cart if user has no existing cart
- [ ] Returns 400 if productId or variantId is missing/invalid
- [ ] Returns 400 if quantity is less than 1
- [ ] Returns 401 if not authenticated
- [ ] Returns existing item with updated quantity if item already in cart
- [ ] Validates product and variant exist before adding
