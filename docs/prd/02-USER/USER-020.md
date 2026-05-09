# USER-020: View Profile

## User Story
> As a logged in user, I can view my profile so I can see my account information.

## API

### GET /api/v1/users/me

**Response (200):**
```json
{
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "email": "user@example.com",
  "phone": "0901234567",
  "role": "BUYER",
  "status": "ACTIVE",
  "avatarUrl": "https://storage.example.com/avatars/550e8400.jpg",
  "createdAt": "2024-01-01T00:00:00Z",
  "lastLoginAt": "2024-01-15T10:30:00Z"
}
```

---

## Acceptance Criteria

- [ ] Authenticated user can view own profile
- [ ] Response includes all fields
- [ ] Unauthenticated request returns 401
