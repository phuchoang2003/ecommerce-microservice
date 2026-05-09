# SELL-020: Invite Staff

## User Story

As a seller (owner/manager), I want to invite staff members to my shop so that they can help manage operations.

## API

**Endpoint:** `POST /api/v1/seller/staff/invite`
**Authentication:** Required (Bearer Token - Seller Role)

### Request Body

```json
{
  "email": "staff@example.com",
  "role": "MANAGER"
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| email | String | Yes | Email address of the staff member |
| role | Enum | Yes | MANAGER or STAFF |

### Response (201 Created)

```json
{
  "id": "660e8400-e29b-41d4-a716-446655440001",
  "shopId": "550e8400-e29b-41d4-a716-446655440000",
  "email": "staff@example.com",
  "role": "MANAGER",
  "status": "PENDING",
  "invitedAt": "2026-04-04T13:00:00Z"
}
```

### Role Permissions

| Role | Permissions |
|------|-------------|
| MANAGER | Full shop management except closing and deleting staff |
| STAFF | View orders, update order status, view products |

### Error Responses

- `400 Bad Request` - Invalid email format or role
- `401 Unauthorized` - Not authenticated
- `403 Forbidden` - User does not have permission to invite staff
- `404 Not Found` - No shop found for user
- `409 Conflict` - User is already a staff member or has an existing invitation

## Acceptance Criteria

- [ ] Owner/Manager can invite new staff by email
- [ ] Invitation creates a pending staff record
- [ ] Email notification is sent to invitee
- [ ] Existing members cannot be re-invited
- [ ] Role must be MANAGER or STAFF
- [ ] Only OWNER can invite MANAGER role
- [ ] MANAGER can only invite STAFF role