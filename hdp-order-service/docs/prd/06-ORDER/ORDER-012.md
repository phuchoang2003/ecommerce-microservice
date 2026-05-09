# ORDER-012: Cancel Order - Buyer Cancels Their Order

## User Story

**As a** buyer
**I want to** cancel my order
**So that** I can cancel my purchase if I change my mind or items are unavailable

---

## API

**Endpoint:** `POST /api/v1/orders/{orderId}/cancel`

### Request Headers
| Header | Value |
|--------|-------|
| Content-Type | application/json |
| Authorization | Bearer {buyer_token} |

### Path Parameters
| Parameter | Type | Description |
|-----------|------|-------------|
| orderId | UUID | Order identifier |

### Request Body
```json
{
  "reason": "Changed my mind"
}
```

### Response (200 OK)
```json
{
  "orderId": "uuid",
  "orderNumber": "ORD-20240404-XXXX",
  "status": "CANCELLED",
  "cancelledAt": "2024-04-04T11:00:00Z",
  "cancellationReason": "Changed my mind",
  "refund": {
    "refundId": "uuid",
    "amount": 174000.00,
    "status": "PENDING",
    "estimatedDays": 5
  }
}
```

### Error Responses
| Status | Code | Description |
|--------|------|-------------|
| 404 | ORDER_NOT_FOUND | Order does not exist |
| 403 | ACCESS_DENIED | Buyer does not own this order |
| 400 | ORDER_NOT_CANCELLABLE | Order status does not allow cancellation |
| 400 | ORDER_ALREADY_PAID | Cannot cancel paid order without refund |

---

## Business Rules

- Orders can only be cancelled in **PENDING** or **PAID** status
- Cancellation triggers inventory release
- Cancellation triggers refund process for paid orders
- Cancellation reason is required and stored for records

---

## Acceptance Criteria

- [ ] Buyer can cancel orders in PENDING status
- [ ] Buyer can cancel orders in PAID status (triggers refund)
- [ ] Cancellation releases reserved inventory
- [ ] Cancellation reason is recorded
- [ ] Cannot cancel orders in PROCESSING, SHIPPED, or DELIVERED status
- [ ] Refund is initiated for paid orders
- [ ] All sub-orders are cancelled along with parent order
