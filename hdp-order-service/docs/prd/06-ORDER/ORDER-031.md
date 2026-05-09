# ORDER-031: Admin Update Status - Admin Manually Updates Order Status

## User Story

**As an** admin
**I want to** manually update an order's status
**So that** I can resolve issues or handle special cases

---

## API

**Endpoint:** `PUT /api/v1/admin/orders/{orderId}/status`

### Request Headers
| Header | Value |
|--------|-------|
| Content-Type | application/json |
| Authorization | Bearer {admin_token} |

### Path Parameters
| Parameter | Type | Description |
|-----------|------|-------------|
| orderId | UUID | Order identifier |

### Request Body
```json
{
  "status": "CANCELLED",
  "reason": "Customer complaint - manual resolution",
  "notifyBuyer": true
}
```

### Response (200 OK)
```json
{
  "orderId": "uuid",
  "orderNumber": "ORD-20240404-XXXX",
  "status": "CANCELLED",
  "previousStatus": "PROCESSING",
  "updatedAt": "2024-04-04T16:00:00Z",
  "adminNote": "Customer complaint - manual resolution",
  "subOrders": [
    {
      "subOrderId": "uuid",
      "status": "CANCELLED"
    }
  ]
}
```

### Error Responses
| Status | Code | Description |
|--------|------|-------------|
| 404 | ORDER_NOT_FOUND | Order does not exist |
| 400 | INVALID_STATUS_TRANSITION | Status transition is not allowed |
| 400 | INVALID_STATUS | Status value is not valid |

---

## Business Rules

- Admin can update any order to any valid status
- Status transitions follow business rules (e.g., cannot go from SHIPPED back to PENDING)
- Reason is required for status changes
- Buyer notification is optional but recommended
- All sub-orders are updated along with parent order

---

## Acceptance Criteria

- [ ] Admin can update status of any order
- [ ] Status transition validation is enforced
- [ ] Reason is recorded in order history
- [ ] Buyer notification is sent if requested
- [ ] All sub-orders are updated to match parent order
- [ ] Previous status is recorded for audit trail
