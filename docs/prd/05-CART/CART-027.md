# CART-027: Remove Invalid Items

## User Story

As a **logged-in customer**, I want to **remove all invalid items from my cart** so that I can **clean up my cart after items become unavailable**.

## API

```
DELETE /api/v1/cart/invalid
Authorization: Bearer {token}
```

### Response Example

```json
{
  "msg": "Invalid items removed",
  "removedItems": [
    {
      "itemId": "item-uuid-11111",
      "productId": "prod-uuid-001",
      "reason": "PRODUCT_UNAVAILABLE"
    }
  ],
  "removedCount": 1,
  "cartTotalItems": 2,
  "cartTotalAmount": 89.98
}
```

## Acceptance Criteria

- [ ] Identifies and removes all invalid items from cart
- [ ] Runs validation before removing items
- [ ] Returns 401 if not authenticated
- [ ] Returns 404 if user has no cart
- [ ] Returns list of removed items with reasons
- [ ] Updates cart totals after removal
- [ ] Returns empty removedItems array if no invalid items found
