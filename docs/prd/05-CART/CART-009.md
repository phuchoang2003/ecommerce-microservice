# CART-009: Move Item to Wishlist

## User Story

As a **logged-in customer**, I want to **move items from my cart to my wishlist** so that I can **save items for later without losing them**.

## API

```
POST /api/v1/cart/items/{itemId}/wishlist
Authorization: Bearer {token}
```

### Response Example

```json
{
  "msg": "Item moved to wishlist",
  "itemId": "item-uuid-11111",
  "wishlistItemId": "wishlist-uuid-55555"
}
```

## Acceptance Criteria

- [ ] Removes item from cart
- [ ] Adds item to user's wishlist
- [ ] Returns 404 if itemId does not exist in cart
- [ ] Returns 403 if itemId belongs to another user's cart
- [ ] Returns 401 if not authenticated
- [ ] Returns 404 if wishlist service is unavailable
- [ ] Cart total is updated after removal
