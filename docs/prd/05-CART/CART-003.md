# CART-003: Update Item Quantity

## User Story

As a **logged-in customer**, I want to **update the quantity of items in my cart** so that I can **adjust my order before checkout**.

## API

```
PUT /api/v1/cart/items/{itemId}
Authorization: Bearer {token}
Content-Type: application/json

{
  "quantity": 3
}
```

### Response Example

```json
{
  "itemId": "item-uuid-11111",
  "productId": "prod-uuid-001",
  "variantId": "var-uuid-001",
  "productName": "Wireless Mouse",
  "variantName": "Black",
  "quantity": 3,
  "unitPrice": 29.99,
  "subtotal": 89.97,
  "selected": true,
  "addedAt": "2026-04-01T10:30:00Z"
}
```

## Acceptance Criteria

- [ ] Updates quantity for the specified item
- [ ] Returns 400 if quantity is less than 1
- [ ] Returns 404 if itemId does not exist in cart
- [ ] Returns 403 if itemId belongs to another user's cart
- [ ] Returns 401 if not authenticated
- [ ] Recalculates subtotal based on new quantity
