# CART-008: Deselect Item

## User Story

As a **logged-in customer**, I want to **deselect items in my cart** so that I can **exclude them from my checkout without removing them**.

## API

```
PUT /api/v1/cart/items/{itemId}/deselect
Authorization: Bearer {token}
```

### Response Example

```json
{
  "itemId": "item-uuid-11111",
  "selected": false,
  "msg": "Item deselected from checkout"
}
```

## Acceptance Criteria

- [ ] Sets item selection status to false
- [ ] Returns 404 if itemId does not exist in cart
- [ ] Returns 403 if itemId belongs to another user's cart
- [ ] Returns 401 if not authenticated
- [ ] Deselected items are excluded from cart summary for checkout
- [ ] Deselected items remain in cart
