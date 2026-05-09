# ORDER-019: Bulk Ship - Seller Ships Multiple Orders at Once

## User Story

**As a** seller
**I want to** ship multiple orders in bulk
**So that** I can efficiently process large volumes of shipments

---

## API

**Endpoint:** `POST /api/v1/seller/orders/bulk-ship`

### Request Headers
| Header | Value |
|--------|-------|
| Content-Type | application/json |
| Authorization | Bearer {seller_token} |

### Request Body
```json
{
  "shipments": [
    {
      "subOrderId": "uuid",
      "trackingNumber": "GHTK123456789",
      "carrier": "GHTK",
      "estimatedDelivery": "2024-04-07"
    },
    {
      "subOrderId": "uuid",
      "trackingNumber": "GHTK987654321",
      "carrier": "GHTK",
      "estimatedDelivery": "2024-04-07"
    }
  ]
}
```

### Response (200 OK)
```json
{
  "successful": [
    {
      "subOrderId": "uuid",
      "status": "SHIPPED",
      "trackingNumber": "GHTK123456789",
      "carrier": "GHTK"
    }
  ],
  "failed": [
    {
      "subOrderId": "uuid",
      "error": "INVALID_STATUS_TRANSITION",
      "msg": "Order must be PROCESSING to transition to SHIPPED"
    }
  ],
  "totalProcessed": 2,
  "successCount": 1,
  "failureCount": 1
}
```

### Error Responses
| Status | Code | Description |
|--------|------|-------------|
| 400 | EMPTY_SHIPMENTS | No shipments provided |
| 400 | TOO_MANY_SHIPMENTS | Maximum 50 shipments per request |

---

## Business Rules

- Maximum 50 orders per bulk shipment request
- Each order must be in PROCESSING status
- Partial success is allowed (some orders may fail)
- Failed orders do not block successful ones

---

## Acceptance Criteria

- [ ] Seller can ship up to 50 orders in one request
- [ ] Successful shipments are processed and returned
- [ ] Failed shipments are reported with error reasons
- [ ] Partial success is supported
- [ ] Each order must be owned by the authenticated seller
- [ ] Tracking information is validated per shipment
