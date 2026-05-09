# CART-007: Select Item

## User Story

As a **logged-in customer**, I want to **select items in my cart** so that I can **choose which items to include in my checkout**.

## API

```
PUT /api/v1/cart/items/{itemId}/select
Authorization: Bearer {token}
```

### Response Example

```json
{
  "itemId": "item-uuid-11111",
  "selected": true,
  "msg": "Item selected for checkout"
}
```

## Acceptance Criteria

- [ ] Sets item selection status to true
- [ ] Returns 404 if itemId does not exist in cart
- [ ] Returns 403 if itemId belongs to another user's cart
- [ ] Returns 401 if not authenticated
- [ ] Selected items are included in cart summary for checkout
