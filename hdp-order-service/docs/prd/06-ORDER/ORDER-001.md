# ORDER-001: Checkout - Reserve Inventory and Create Payment Intent

## User Story

**As a** buyer
**I want to** checkout my cart by reserving inventory and creating a payment intent
**So that** I can secure the items I want to purchase before making payment

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
  "paymentMethod": "CREDIT_CARD | BANK_TRANSFER | E_WALLET | COD",
  "couponCodes": ["SUMMER2024"]
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
  "shippingFee": 25000.00,
  "discount": 15000.00,
  "tax": 14000.00,
  "totalAmount": 174000.00,
  "expiresAt": "2024-04-04T12:34:56Z",
  "subOrders": [
    {
      "subOrderId": "uuid",
      "sellerId": "uuid",
      "sellerName": "Store Name",
      "status": "PENDING",
      "items": [...]
    }
  ]
}
```

### Error Responses
| Status | Code | Description |
|--------|------|-------------|
| 400 | INVALID_CART | Cart is empty or invalid |
| 400 | INSUFFICIENT_INVENTORY | Not enough stock for requested items |
| 400 | INVALID_ADDRESS | Shipping address not found |
| 400 | INVALID_COUPON | Coupon code is invalid or expired |

---

## Acceptance Criteria

- [ ] Buyer can checkout with cart items
- [ ] Inventory is reserved for 30 minutes (PENDING status)
- [ ] Payment intent is created with payment provider
- [ ] Coupon codes are applied if valid
- [ ] Order expires after 30 minutes without payment
- [ ] Multi-seller carts create multiple SubOrders
- [ ] Order number is auto-generated (ORD-YYYYMMDD-XXXX)
