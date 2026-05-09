# CART-018: Merge Guest Cart to User Cart

## User Story

As a **logged-in customer**, I want to **merge my guest cart into my user cart** so that I can **transfer items saved as a guest to my account after logging in**.

## API

```
POST /api/v1/cart/merge
Authorization: Bearer {token}
Content-Type: application/json

{
  "guestId": "guest-uuid-abc123"
}
```

### Response Example

```json
{
  "msg": "Cart merged successfully",
  "mergedItemsCount": 3,
  "cart": {
    "cartId": "cart-uuid-12345",
    "userId": "user-uuid-67890",
    "items": [...],
    "totalItems": 5,
    "totalAmount": 199.97
  }
}
```

## Acceptance Criteria

- [ ] Merges all guest cart items into user cart
- [ ] Adds items from guest cart that user doesn't already have
- [ ] Increases quantity for items that already exist in user cart
- [ ] Deletes guest cart after successful merge
- [ ] Returns 404 if guestId does not exist
- [ ] Returns 404 if user has no cart (creates one first)
- [ ] Returns 401 if not authenticated
- [ ] Returns 200 on successful merge
