# CART-016: Guest Add Item

## User Story

As a **guest customer**, I want to **add items to my guest cart** so that I can **shop without creating an account**.

## API

```
POST /api/v1/cart/guest/{guestId}/items
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
  "itemId": "item-uuid-66666",
  "productId": "prod-uuid-001",
  "variantId": "var-uuid-001",
  "productName": "Wireless Mouse",
  "variantName": "Black",
  "quantity": 1,
  "unitPrice": 29.99,
  "subtotal": 29.99,
  "addedAt": "2026-04-04T15:30:00Z"
}
```

## Acceptance Criteria

- [ ] Adds item to guest's cart
- [ ] Returns 404 if guestId does not exist
- [ ] Returns 400 if productId, variantId, or quantity is invalid
- [ ] No authentication required
- [ ] Creates cart with guestId if cart does not exist
- [ ] Merges quantity if item already exists in cart
