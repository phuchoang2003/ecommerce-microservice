# ORDER-017: Process Order - Seller Starts Processing Paid Order

## User Story

**As a** seller
**I want to** start processing a paid order
**So that** I can prepare the items for shipment

---

## API

**Endpoint:** `PUT /api/v1/seller/orders/{orderId}/process`

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
  "note": "Preparing items for shipment"
}
```

### Response (200 OK)
```json
{
  "subOrderId": "uuid",
  "orderId": "uuid",
  "status": "PROCESSING",
  "processedAt": "2024-04-04T14:00:00Z",
  "note": "Preparing items for shipment"
}
```

### Error Responses
| Status | Code | Description |
|--------|------|-------------|
| 404 | ORDER_NOT_FOUND | Sub-order does not exist |
| 403 | ACCESS_DENIED | Seller does not own this sub-order |
| 400 | INVALID_STATUS_TRANSITION | Order must be PAID to transition to PROCESSING |

---

## Business Rules

- Only sub-orders in **PAID** status can be processed
- Processing indicates seller has started fulfilling the order
- Once processing starts, cancellation by buyer is not allowed
- Seller note is optional but recommended for tracking

---

## Acceptance Criteria

- [ ] Seller can process orders in PAID status
- [ ] Status changes from PAID to PROCESSING
- [ ] Processed timestamp is recorded
- [ ] Cannot process orders that are not in PAID status
- [ ] Seller can add a note during processing
- [ ] Parent order status updates based on sub-order statuses
