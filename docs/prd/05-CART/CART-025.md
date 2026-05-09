# CART-025: Validate Cart

## User Story

As a **logged-in customer**, I want to **validate my entire cart** so that I can **ensure all items are still available and prices are current before checkout**.

## API

```
POST /api/v1/cart/validate
Authorization: Bearer {token}
```

### Response Example

```json
{
  "valid": false,
  "invalidItems": [
    {
      "itemId": "item-uuid-11111",
      "productId": "prod-uuid-001",
      "reason": "PRODUCT_UNAVAILABLE",
      "msg": "This product is no longer available"
    }
  ],
  "priceChanged": [
    {
      "itemId": "item-uuid-22222",
      "productId": "prod-uuid-002",
      "oldPrice": 29.99,
      "newPrice": 34.99,
      "msg": "Price has been updated"
    }
  ],
  "validatedAt": "2026-04-04T16:00:00Z"
}
```

## Acceptance Criteria

- [ ] Validates all items in cart
- [ ] Checks product availability
- [ ] Checks variant availability
- [ ] Verifies current prices
- [ ] Returns 401 if not authenticated
- [ ] Returns 404 if user has no cart
- [ ] Returns list of invalid items with reasons
- [ ] Returns list of items with price changes
- [ ] Sets valid to true if all items pass validation
