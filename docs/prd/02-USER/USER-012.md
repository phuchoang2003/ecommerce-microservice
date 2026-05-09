# USER-012: Logout

## User Story
> As a user, I can logout so my session is terminated and token is invalidated.

## API

### POST /api/v1/users/logout

**Request:**
```json
{
  "refreshToken": "dGhpcyBpcyBhIHJlZnJlc2g..."
}
```

**Response (200):**
```json
{
  "success": true
}
```

**Side Effects:**
- Add refresh token to blacklist (Redis, TTL = 7 days)

---

## Acceptance Criteria

- [ ] Logout successfully blacklists token
- [ ] Blacklisted token cannot be used to refresh
- [ ] Access token still works until expiry
- [ ] Subsequent logout with same token still succeeds (idempotent)
