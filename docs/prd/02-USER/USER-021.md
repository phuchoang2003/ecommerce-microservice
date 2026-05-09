# USER-021: Update Profile

## User Story
> As a logged in user, I can update my phone and avatar so my profile stays current.

## API

### PUT /api/v1/users/me

**Request:**
```json
{
  "phone": "0912345678",
  "avatarUrl": "https://storage.example.com/avatars/new-avatar.jpg"
}
```

**Validation:**
| Field | Rule |
|-------|------|
| phone | Valid Vietnam format (10-11 digits), unique |
| avatarUrl | Valid URL format |

**Response (200):**
```json
{
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "email": "user@example.com",
  "phone": "0912345678",
  "role": "BUYER",
  "status": "ACTIVE",
  "avatarUrl": "https://storage.example.com/avatars/new-avatar.jpg",
  "updatedAt": "2024-01-15T12:00:00Z"
}
```

**Note:** Email cannot be changed via this endpoint.

---

## Acceptance Criteria

- [ ] Can update phone
- [ ] Can update avatarUrl
- [ ] Cannot change email
- [ ] Invalid phone returns 400
- [ ] Invalid avatarUrl returns 400
- [ ] Duplicate phone returns 409
