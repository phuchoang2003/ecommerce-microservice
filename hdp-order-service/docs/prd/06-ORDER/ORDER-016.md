# ORDER-016: Get Seller Order - Seller Views Order Details

## User Story

**As a** seller
**I want to** view detailed information about a specific order
**So that** I can prepare and ship the items to the customer

---

## API

**Endpoint:** `GET /api/v1/seller/orders/{orderId}`

### Request Headers
| Header | Value |
|--------|-------|
| Authorization | Bearer {seller_token} |

### Path Parameters
| Parameter | Type | Description |
|-----------|------|-------------|
| orderId | UUID | Sub-order identifier |

### Response (200 OK)
```json
{
  "subOrderId": "uuid",
  "orderId": "uuid",
  "orderNumber": "ORD-20240404-XXXX",
  "status": "PAID",
  "paymentMethod": "CREDIT_CARD",
  "shippingAddress": {
    "recipientName": "John Doe",
    "phone": "+84-123-456-789",
    "addressLine1": "123 Main St",
    "addressLine2": "Apt 4B",
    "city": "Ho Chi Minh City",
    "state": "HCMC",
    "postalCode": "70000",
    "country": "Vietnam"
  },
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
  ],
  "totalAmount": 100000.00,
  "createdAt": "2024-04-04T10:00:00Z"
}
```

### Error Responses
| Status | Code | Description |
|--------|------|-------------|
| 404 | ORDER_NOT_FOUND | Sub-order does not exist |
| 403 | ACCESS_DENIED | Seller does not own this sub-order |

---

## Acceptance Criteria

- [ ] Seller can view complete sub-order details
- [ ] Seller can only view their own sub-orders
- [ ] Response includes full shipping address for fulfillment
- [ ] Response includes all items in the sub-order
- [ ] Buyer contact information is available for shipping queries
