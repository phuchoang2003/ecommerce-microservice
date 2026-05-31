# PROD-026: Commit Reservation (Internal)

## User Story
As an **internal service** (Order Service), I want to commit inventory reservations when orders are completed, so that reserved stock is permanently deducted.

## API
```
POST /internal/inventory/commit
```

### Headers
| Header | Value | Required |
|--------|-------|----------|
| Authorization | Bearer {service-token} | Yes |

### Request Body
```json
{
  "reservationId": "uuid-reservation"
}
```

### Response (200 OK)
```json
{
  "success": true,
  "data": {
    "reservationId": "uuid-reservation",
    "status": "COMMITTED",
    "committedItems": [
      {
        "variantId": "uuid-variant",
        "quantity": 2,
        "committed": true
      }
    ]
  }
}
```

### Error Responses
- **400 Bad Request**: Invalid input or reservation expired
- **401 Unauthorized**: Not authenticated
- **404 Not Found**: Reservation does not exist
- **409 Conflict**: Reservation already released or committed

## Acceptance Criteria
- [ ] Only accessible with internal service token
- [ ] Permanently deducts stock (removes from reservedStock)
- [ ] Reservation status changes to COMMITTED
- [ ] Returns 404 if reservation does not exist
- [ ] Returns 409 if reservation already released or committed
- [ ] soldCount on product is incremented
