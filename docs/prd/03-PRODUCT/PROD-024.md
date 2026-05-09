# PROD-024: Reserve Inventory (Internal)

## User Story
As an **internal service** (Order Service), I want to reserve inventory for an order, so that stock is not oversold.

## API
```
POST /internal/inventory/reserve
```

### Headers
| Header | Value | Required |
|--------|-------|----------|
| Authorization | Bearer {service-token} | Yes |

### Request Body
```json
{
  "orderId": "uuid-order",
  "items": [
    {
      "variantId": "uuid-variant",
      "quantity": 2
    }
  ]
}
```

### Response (200 OK)
```json
{
  "success": true,
  "data": {
    "reservationId": "uuid-reservation",
    "orderId": "uuid-order",
    "items": [
      {
        "variantId": "uuid-variant",
        "quantity": 2,
        "reserved": true
      }
    ],
    "expiresAt": "2026-04-04T14:30:00Z"
  }
}
```

### Error Responses
- **400 Bad Request**: Invalid input or insufficient stock
- **401 Unauthorized**: Not authenticated
- **409 Conflict**: Insufficient stock for one or more items

## Acceptance Criteria
- [ ] Only accessible with internal service token
- [ ] Creates inventory reservation with 30-minute expiry
- [ ] Decrements availableStock (stock - reservedStock)
- [ ] Returns 409 if insufficient stock available
- [ ] All items must be reserved or entire reservation fails (atomic)
- [ ] Reservation auto-expires after 30 minutes
