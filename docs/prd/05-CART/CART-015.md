# CART-015: Guest Create Cart

## User Story

As a **guest customer**, I want to **create a cart using a guest ID** so that I can **start shopping without creating an account**.

## API

```
POST /api/v1/cart/guest
Content-Type: application/json

{
  "guestId": "guest-uuid-abc123"
}
```

### Response Example

```json
{
  "cartId": "cart-uuid-67890",
  "guestId": "guest-uuid-abc123",
  "items": [],
  "totalItems": 0,
  "totalAmount": 0,
  "createdAt": "2026-04-04T15:00:00Z"
}
```

## Acceptance Criteria

- [ ] Creates a new cart for the guest ID
- [ ] Returns 400 if guestId is missing or invalid
- [ ] Returns existing cart if guestId already has a cart
- [ ] No authentication required
- [ ] Guest cart has 30-day expiration
