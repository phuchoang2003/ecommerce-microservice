# SELL-021: List Staff

## User Story

As a seller (owner/manager), I want to list all staff members in my shop so that I can manage my team.

## API

**Endpoint:** `GET /api/v1/seller/staff`
**Authentication:** Required (Bearer Token - Seller Role)

### Query Parameters

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| status | String | No | ACTIVE | Filter by status: ACTIVE, INACTIVE, PENDING |
| page | Integer | No | 1 | Page number |
| size | Integer | No | 20 | Items per page (max 100) |

### Response (200 OK)

```json
{
  "content": [
    {
      "id": "660e8400-e29b-41d4-a716-446655440001",
      "userId": "789e0123-e89b-12d3-a456-426614174001",
      "email": "staff@example.com",
      "role": "MANAGER",
      "status": "ACTIVE",
      "invitedAt": "2026-04-04T13:00:00Z",
      "joinedAt": "2026-04-04T14:30:00Z"
    }
  ],
  "page": 1,
  "size": 20,
  "totalElements": 5,
  "totalPages": 1
}
```

### Error Responses

- `401 Unauthorized` - Not authenticated
- `403 Forbidden` - User does not have permission to view staff
- `404 Not Found` - No shop found for user

## Acceptance Criteria

- [ ] Owner/Manager can view all staff members
- [ ] Staff can view their own status only
- [ ] Results can be filtered by status
- [ ] Results are paginated
- [ ] PENDING invitations are included in results
- [ ] OWNER is not listed in staff (excluded from list)