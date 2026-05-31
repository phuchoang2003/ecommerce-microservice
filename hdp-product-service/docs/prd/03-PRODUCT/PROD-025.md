# PROD-025: Release Reservation (Internal)

## User Story
As an **internal service** (Order Service), I want to release inventory reservations when orders are cancelled, so that stock becomes available again.

## API
```
POST /internal/inventory/release
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
    "status": "RELEASED",
    "releasedItems": [
      {
        "variantId": "uuid-variant",
        "quantity": 2,
        "released": true
      }
    ]
  }
}
```

### Error Responses
- **400 Bad Request**: Invalid input
- **401 Unauthorized**: Not authenticated
- **404 Not Found**: Reservation does not exist

## Acceptance Criteria
- [ ] Only accessible with internal service token
- [ ] Releases reserved stock back to available
- [ ] Reservation status changes to RELEASED
- [ ] Returns 404 if reservation does not exist
- [ ] Idempotent: releasing already released reservation returns success
