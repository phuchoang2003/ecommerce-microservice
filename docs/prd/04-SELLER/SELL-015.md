# SELL-015: Close Shop

## User Story

As a seller, I want to close my shop so that I can permanently stop selling on the platform.

## API

**Endpoint:** `PUT /api/v1/seller/shops/me/close`
**Authentication:** Required (Bearer Token - Seller Role)

### Response (200 OK)

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Acme Electronics",
  "status": "INACTIVE",
  "closedAt": "2026-04-04T12:00:00Z"
}
```

### Business Rules

- Closing a shop is permanent and cannot be undone
- All pending orders must be completed or cancelled before closing
- Staff accounts will be deactivated
- User can reapply for seller after 30 days

### Error Responses

- `401 Unauthorized` - Not authenticated
- `403 Forbidden` - User does not have permission (only OWNER can close)
- `404 Not Found` - No shop found for user
- `409 Conflict` - Shop has pending orders that must be resolved first

## Acceptance Criteria

- [ ] Only OWNER can close the shop
- [ ] Shop status changes to INACTIVE
- [ ] Closed shops cannot be reopened
- [ ] All staff accounts are deactivated
- [ ] User cannot apply for new seller account for 30 days
- [ ] Pending orders must be resolved before closing