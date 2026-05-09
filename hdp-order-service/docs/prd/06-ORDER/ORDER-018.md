# ORDER-018: Ship Order - Seller Ships Order with Tracking

## User Story

**As a** seller
**I want to** ship an order and add tracking information
**So that** the buyer can track their package delivery

---

## API

**Endpoint:** `PUT /api/v1/seller/orders/{orderId}/ship`

### Request Headers
| Header | Value |
|--------|-------|
| Content-Type | application/json |
| Authorization | Bearer {seller_token} |

### Path Parameters
| Parameter | Type | Description |
|-----------|------|-------------|
| orderId | UUID | Sub-order identifier |

### Request Body
```json
{
  "trackingNumber": "GHTK123456789",
  "carrier": "GHTK",
  "estimatedDelivery": "2024-04-07",
  "note": "Package left warehouse"
}
```

### Response (200 OK)
```json
{
  "subOrderId": "uuid",
  "orderId": "uuid",
  "status": "SHIPPED",
  "trackingNumber": "GHTK123456789",
  "carrier": "GHTK",
  "estimatedDelivery": "2024-04-07",
  "shippedAt": "2024-04-04T16:00:00Z",
  "note": "Package left warehouse"
}
```

### Error Responses
| Status | Code | Description |
|--------|------|-------------|
| 404 | ORDER_NOT_FOUND | Sub-order does not exist |
| 403 | ACCESS_DENIED | Seller does not own this sub-order |
| 400 | INVALID_STATUS_TRANSITION | Order must be PROCESSING to transition to SHIPPED |
| 400 | MISSING_TRACKING_NUMBER | Tracking number is required |

---

## Business Rules

- Only sub-orders in **PROCESSING** status can be shipped
- Tracking number and carrier are required
- Estimated delivery date is optional but recommended
- Shipping triggers notification to buyer

---

## Acceptance Criteria

- [ ] Seller can ship orders in PROCESSING status
- [ ] Status changes from PROCESSING to SHIPPED
- [ ] Tracking number is recorded
- [ ] Carrier is recorded
- [ ] Estimated delivery date is recorded
- [ ] Buyer receives shipping notification
- [ ] Shipped timestamp is recorded
