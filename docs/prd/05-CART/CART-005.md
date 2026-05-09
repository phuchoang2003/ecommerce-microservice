# CART-005: Clear Cart

## User Story

As a **logged-in customer**, I want to **clear all items from my cart** so that I can **start fresh with a new shopping list**.

## API

```
DELETE /api/v1/cart
Authorization: Bearer {token}
```

### Response Example

```json
{
  "msg": "Cart cleared successfully",
  "cartId": "cart-uuid-12345"
}
```

## Acceptance Criteria

- [ ] Removes all items from user's cart
- [ ] Returns 404 if user has no cart
- [ ] Returns 401 if not authenticated
- [ ] Cart structure remains but items array is empty
- [ ] Returns 200 on successful clear
