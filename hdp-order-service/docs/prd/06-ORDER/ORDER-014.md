# ORDER-014: Confirm Payment - Webhook Receives Payment Confirmation

## User Story

**As a** payment service
**I want to** notify the order service when a payment is confirmed
**So that** orders can be updated from PENDING to PAID status

---

## API

**Endpoint:** `POST /api/v1/orders/{orderId}/confirm-payment`

### Request Headers
| Header | Value |
|--------|-------|
| Content-Type | application/json |
| X-Webhook-Signature | {hmac_signature} |

### Path Parameters
| Parameter | Type | Description |
|-----------|------|-------------|
| orderId | UUID | Order identifier |

### Request Body
```json
{
  "paymentIntentId": "pi_xxx",
  "status": "SUCCEEDED",
  "amount": 174000.00,
  "currency": "VND",
  "paidAt": "2024-04-04T10:05:00Z",
  "paymentMethod": "CREDIT_CARD",
  "transactionId": "txn_xxx"
}
```

### Response (200 OK)
```json
{
  "orderId": "uuid",
  "status": "PAID",
  "paidAt": "2024-04-04T10:05:00Z",
  "subOrders": [
    {
      "subOrderId": "uuid",
      "status": "PAID"
    }
  ]
}
```

### Error Responses
| Status | Code | Description |
|--------|------|-------------|
| 400 | INVALID_SIGNATURE | Webhook signature verification failed |
| 400 | PAYMENT_MISMATCH | Payment amount does not match order |
| 404 | ORDER_NOT_FOUND | Order does not exist |
| 400 | ORDER_ALREADY_PAID | Order is not in PENDING status |
| 400 | PAYMENT_FAILED | Payment status is not SUCCEEDED |

---

## Business Rules

- Webhook signature must be verified using HMAC
- Payment amount must match order total
- Idempotency: duplicate webhooks are handled gracefully
- Only PENDING orders can be transitioned to PAID

---

## Acceptance Criteria

- [ ] Webhook signature is verified before processing
- [ ] Payment amount is validated against order total
- [ ] Order status changes from PENDING to PAID
- [ ] All sub-order statuses change from PENDING to PAID
- [ ] Duplicate webhooks are handled idempotently
- [ ] Failed payments update order status to PAYMENT_FAILED
- [ ] Paid timestamp is recorded accurately
