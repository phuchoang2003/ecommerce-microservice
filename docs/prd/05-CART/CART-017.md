# CART-017: Guest Get Cart

## User Story

As a **guest customer**, I want to **view my guest cart** so that I can **see what items I have added**.

## API

```
GET /api/v1/cart/guest/{guestId}
```

### Response Example

```json
{
  "cartId": "cart-uuid-67890",
  "guestId": "guest-uuid-abc123",
  "items": [
    {
      "itemId": "item-uuid-66666",
      "productId": "prod-uuid-001",
      "variantId": "var-uuid-001",
      "productName": "Wireless Mouse",
      "variantName": "Black",
      "quantity": 1,
      "unitPrice": 29.99,
      "subtotal": 29.99,
      "selected": true,
      "addedAt": "2026-04-04T15:30:00Z"
    }
  ],
  "totalItems": 1,
  "totalAmount": 29.99,
  "expiresAt": "2026-05-04T15:00:00Z"
}
```

## Acceptance Criteria

- [ ] Returns cart for the specified guest ID
- [ ] Returns 404 if guestId does not exist
- [ ] No authentication required
- [ ] Returns empty cart if guest has no items
- [ ] Includes expiration time for guest cart
