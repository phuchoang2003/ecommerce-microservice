# CART-004: Remove Item

## User Story

As a **logged-in customer**, I want to **remove items from my cart** so that I can **remove products I no longer want**.

## API

```
DELETE /api/v1/cart/items/{itemId}
Authorization: Bearer {token}
```

### Response Example

```json
{
  "msg": "Item removed successfully",
  "itemId": "item-uuid-11111"
}
```

## Acceptance Criteria

- [ ] Removes the specified item from cart
- [ ] Returns 404 if itemId does not exist in cart
- [ ] Returns 403 if itemId belongs to another user's cart
- [ ] Returns 401 if not authenticated
- [ ] Updates cart total after removal
