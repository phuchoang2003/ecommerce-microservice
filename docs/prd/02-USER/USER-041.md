# USER-041: Admin Update User Status

## User Story
> As an admin, I can ban or unban a user so I can enforce platform policies and handle policy violations.

## API

### PUT /api/v1/admin/users/{userId}/status

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| userId | UUID | The user ID to update |

**Request:**
```json
{
  "status": "BANNED",
  "reason": "Policy violation - spam content"
}
```

**Validation:**
| Field | Rule |
|-------|------|
| status | Required, must be "ACTIVE" or "BANNED" |
| reason | Required when status is "BANNED" |

**Response (200):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "email": "user@example.com",
  "phone": "0901234567",
  "role": "BUYER",
  "status": "BANNED",
  "avatarUrl": "https://cdn.example.com/avatars/user.jpg",
  "createdAt": "2024-01-10T08:00:00Z",
  "updatedAt": "2024-01-25T16:00:00Z"
}
```

**Business Rules:**
- BANNED users cannot login
- Banned users' listings are hidden from buyers
- Reason is recorded for audit purposes

**Authorization:**
- Requires ADMIN role

**Side Effects:**
- If user is currently logged in, their refresh tokens are revoked
- Publish `user.status_changed` event to Kafka topic `user-events`

**Error Responses:**
- 400: Invalid status value
- 404: User not found
- 409: Cannot ban already banned user / Cannot activate non-banned user

---

## Acceptance Criteria

- [ ] Admin can ban a user with reason
- [ ] Admin can unban a user (set to ACTIVE)
- [ ] Banned users receive 401 on login attempts
- [ ] Reason is stored for banned users
- [ ] 400 returned for invalid status value
- [ ] 404 returned if user does not exist
- [ ] 409 returned if status already matches
- [ ] 403 returned if not ADMIN role
- [ ] 401 returned if not authenticated
- [ ] Kafka event published on status change
