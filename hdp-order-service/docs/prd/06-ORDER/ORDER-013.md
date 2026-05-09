# ORDER-013: Track Order - Buyer Tracks Order Delivery Status

## User Story

**As a** buyer
**I want to** track my order delivery status
**So that** I can know when my order will arrive

---

## API

**Endpoint:** `GET /api/v1/orders/{orderId}/tracking`

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
  "currentStatus": "SHIPPED",
  "estimatedDelivery": "2024-04-07",
  "subOrders": [
    {
      "subOrderId": "uuid",
      "sellerName": "Store Name",
      "status": "SHIPPED",
      "trackingNumber": "TRACK123456789",
      "carrier": "GHTK",
      "estimatedDelivery": "2024-04-07",
      "timeline": [
        {
          "status": "SHIPPED",
          "description": "Package has been shipped",
          "timestamp": "2024-04-05T10:00:00Z",
          "location": "Ho Chi Minh City Distribution Center"
        },
        {
          "status": "PROCESSING",
          "description": "Seller is preparing the package",
          "timestamp": "2024-04-04T15:00:00Z",
          "location": "Seller Warehouse"
        },
        {
          "status": "PAID",
          "description": "Payment confirmed",
          "timestamp": "2024-04-04T10:05:00Z",
          "location": null
        },
        {
          "status": "PENDING",
          "description": "Order placed",
          "timestamp": "2024-04-04T10:00:00Z",
          "location": null
        }
      ]
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

- [ ] Buyer can track all sub-orders in a single request
- [ ] Timeline shows chronological status changes
- [ ] Tracking number and carrier are shown when available
- [ ] Estimated delivery date is provided when available
- [ ] Each sub-order has its own tracking timeline
