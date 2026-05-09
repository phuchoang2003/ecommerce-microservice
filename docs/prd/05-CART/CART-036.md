# CART-036: Remove Coupon

## User Story

As a **logged-in customer**, I want to **remove a coupon from my cart** so that I can **change to a different discount offer**.

## API

```
DELETE /api/v1/cart/coupons/{couponCode}
Authorization: Bearer {token}
```

### Response Example

```json
{
  "msg": "Coupon removed successfully",
  "couponCode": "SAVE10",
  "previousDiscount": 14.99,
  "cartTotal": 134.98
}
```

## Acceptance Criteria

- [ ] Removes the specified coupon from cart
- [ ] Returns 404 if couponCode is not applied to cart
- [ ] Returns 401 if not authenticated
- [ ] Returns 404 if user has no cart
- [ ] Recalculates cart total after coupon removal
- [ ] Returns previous discount amount for reference
