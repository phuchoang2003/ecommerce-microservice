# SELL-022: Update Staff Role

## User Story

As a seller (owner/manager), I want to update a staff member's role so that I can adjust their permissions.

## API

**Endpoint:** `PUT /api/v1/seller/staff/{staffId}/role`
**Authentication:** Required (Bearer Token - Seller Role)

### Path Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| staffId | UUID | Staff member unique identifier |

### Request Body

```json
{
  "role": "STAFF"
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| role | Enum | Yes | MANAGER or STAFF |

### Response (200 OK)

```json
{
  "id": "660e8400-e29b-41d4-a716-446655440001",
  "userId": "789e0123-e89b-12d3-a456-426614174001",
  "email": "staff@example.com",
  "role": "STAFF",
  "status": "ACTIVE",
  "invitedAt": "2026-04-04T13:00:00Z",
  "joinedAt": "2026-04-04T14:30:00Z"
}
```

### Error Responses

- `400 Bad Request` - Invalid role value
- `401 Unauthorized` - Not authenticated
- `403 Forbidden` - User does not have permission
- `404 Not Found` - Staff member not found in shop
- `409 Conflict` - Cannot change OWNER role

## Acceptance Criteria

- [ ] Owner can change any staff role (MANAGER/STAFF)
- [ ] Manager cannot change roles (only OWNER can)
- [ ] Cannot promote STAFF to MANAGER without OWNER approval
- [ ] Role change is recorded with timestamp
- [ ] Notification is sent to the staff member
- [ ] Cannot modify OWNER role