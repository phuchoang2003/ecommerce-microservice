# USER-011: Refresh Token

## User Story
> As a user with an expired access token, I can refresh it so I can continue using the app without re-login.

## API

### POST /api/v1/users/refresh

**Request:**
```json
{
  "refreshToken": "dGhpcyBpcyBhIHJlZnJlc2g..."
}
```

**Response (200):**
```json
{
  "accessToken": "eyJhbGciOiJSUzI1NiIs...",
  "expiresIn": 3600
}
```

**Error Response (401):**
```json
{
  "error": "INVALID_REFRESH_TOKEN",
  "msg": "Refresh token is invalid or expired"
}
```

---

## Acceptance Criteria

- [ ] Valid refresh token returns new access token
- [ ] Invalid refresh token returns 401
- [ ] Expired refresh token returns 401
- [ ] Blacklisted refresh token returns 401
- [ ] Access token expires in 1 hour
