# ORDER-011: Get Order - Buyer Views Order Details

## User Story

**As a** buyer
**I want to** view detailed information about a specific order
**So that** I can see the complete details of my purchase

---

## API

**Endpoint:** `GET /api/v1/orders/{orderId}`

### Request Headers
| Header | Value |
|--------|-------|
| Authorization | Bearer {buyer_token} |

### Path Parameters
| Parameter | Type | Description |
|-----------|------|-------------|
| orderId | UUID | Order identifier |

### Response (200 OK)
```json
{
  "orderId": "uuid",
  "orderNumber": "ORD-20240404-XXXX",
  "status": "PROCESSING",
  "paymentMethod": "CREDIT_CARD",
  "paymentIntentId": "pi_xxx",
  "shippingAddress": {
    "id": "uuid",
    "recipientName": "John Doe",
    "phone": "+84-123-456-789",
    "addressLine1": "123 Main St",
    "addressLine2": "Apt 4B",
    "city": "Ho Chi Minh City",
    "state": "HCMC",
    "postalCode": "70000",
    "country": "Vietnam"
  },
  "subtotal": 150000.00,
  "shippingFee": 25000.00,
  "discount": 15000.00,
  "tax": 14000.00,
  "totalAmount": 174000.00,
  "createdAt": "2024-04-04T10:00:00Z",
  "updatedAt": "2024-04-04T14:30:00Z",
  "expiresAt": "2024-04-04T10:30:00Z",
  "subOrders": [
    {
      "subOrderId": "uuid",
      "sellerId": "uuid",
      "sellerName": "Store Name",
      "status": "PROCESSING",
      "trackingNumber": null,
      "carrier": null,
      "estimatedDelivery": "2024-04-07",
      "items": [
        {
          "itemId": "uuid",
          "productId": "uuid",
          "productName": "Product Name",
          "variantName": "Size: L, Color: Blue",
          "price": 50000.00,
          "quantity": 2,
          "subtotal": 100000.00
        }
      ]
    }
  ],
  "appliedCoupons": [
    {
      "code": "SUMMER2024",
      "type": "PERCENTAGE",
      "value": 15000.00
    }
  ]
}
```

### Error Responses
| Status | Code | Description |
|--------|------|-------------|
| 404 | ORDER_NOT_FOUND | Order does not exist |
| 403 | ACCESS_DENIED | Buyer does not own this order |

---

## Acceptance Criteria

- [ ] Buyer can view complete order details including all sub-orders
- [ ] Buyer can only view their own orders
- [ ] Order includes full shipping address details
- [ ] Order includes all order items with product snapshots
- [ ] Applied coupons are listed with discount values
- [ ] Sub-order status reflects current fulfillment status
