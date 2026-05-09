# CART-035: Apply Coupon

## User Story

As a **logged-in customer**, I want to **apply a coupon code to my cart** so that I can **receive discounts on my purchase**.

## API

```
POST /api/v1/cart/coupons
Authorization: Bearer {token}
Content-Type: application/json

{
  "couponCode": "SAVE10"
}
```

### Success Response Example

```json
{
  "success": true,
  "couponCode": "SAVE10",
  "discountType": "PERCENTAGE",
  "discountValue": 10,
  "discountAmount": 14.99,
  "msg": "Coupon applied successfully"
}
```

### Failure Response Example

```json
{
  "success": false,
  "couponCode": "EXPIRED50",
  "reason": "COUPON_EXPIRED",
  "msg": "This coupon has expired"
}
```

## Acceptance Criteria

- [ ] Validates coupon code before applying
- [ ] Returns 400 if couponCode is missing
- [ ] Returns 401 if not authenticated
- [ ] Returns 404 if user has no cart
- [ ] Checks coupon expiration date
- [ ] Checks coupon minimum purchase requirement
- [ ] Checks coupon usage limits
- [ ] Returns discount amount based on coupon type (percentage or fixed)
- [ ] Shows error reason if coupon is invalid
