# ORDER-002: Checkout with Coupon - Apply Coupon Codes During Checkout

## User Story

**As a** buyer
**I want to** apply coupon codes during checkout
**So that** I can receive discounts on my order

---

## API

**Endpoint:** `POST /api/v1/checkout`

### Request Headers
| Header | Value |
|--------|-------|
| Content-Type | application/json |
| Authorization | Bearer {buyer_token} |

### Request Body
```json
{
  "cartId": "uuid",
  "shippingAddressId": "uuid",
  "paymentMethod": "CREDIT_CARD",
  "couponCodes": ["SUMMER2024", "FREESHIP"]
}
```

### Response (201 Created)
```json
{
  "orderId": "uuid",
  "orderNumber": "ORD-20240404-XXXX",
  "status": "PENDING",
  "paymentIntentId": "pi_xxx",
  "subtotal": 150000.00,
  "shippingFee": 0.00,
  "discount": 22500.00,
  "tax": 12750.00,
  "totalAmount": 140250.00,
  "appliedCoupons": [
    {
      "code": "SUMMER2024",
      "type": "PERCENTAGE",
      "value": 15000.00
    },
    {
      "code": "FREESHIP",
      "type": "FREE_SHIPPING",
      "value": 25000.00
    }
  ],
  "expiresAt": "2024-04-04T12:34:56Z"
}
```

### Error Responses
| Status | Code | Description |
|--------|------|-------------|
| 400 | INVALID_COUPON | Coupon code is invalid or expired |
| 400 | COUPON_ALREADY_USED | Coupon was already used by this buyer |
| 400 | COUPON_MINIMUM_NOT_MET | Order total does not meet coupon minimum |
| 400 | COUPON_NOT_APPLICABLE | Coupon cannot be used with some items |

---

## Acceptance Criteria

- [ ] Multiple coupon codes can be applied in a single checkout
- [ ] Discount is calculated correctly for each coupon type (percentage, fixed, free shipping)
- [ ] Coupon validation includes expiry, usage limits, and minimum order requirements
- [ ] Applied coupons are listed in the response with their values
- [ ] Combined discounts do not exceed order subtotal
- [ ] Invalid coupons return clear error messages
