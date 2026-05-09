# USER-010: Login

## User Story
> As a user, I can login with email or phone and password so I can access my account.

## API

### POST /api/v1/users/login

**Request (with email):**
```json
{
  "email": "user@example.com",
  "password": "SecurePass123"
}
```

**Request (with phone):**
```json
{
  "phone": "0901234567",
  "password": "SecurePass123"
}
```

**Response (200):**
```json
{
  "accessToken": "eyJhbGciOiJSUzI1NiIs...",
  "refreshToken": "dGhpcyBpcyBhIHJlZnJlc2g...",
  "tokenType": "Bearer",
  "expiresIn": 3600,
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "role": "BUYER"
}
```

**Side Effects:**
- Update `lastLoginAt` timestamp
- Log login event with IP address

**Error Response (401):**
```json
{
  "error": "INVALID_CREDENTIALS",
  "msg": "Invalid email or password"
}
```

**Error Response (403):**
```json
{
  "error": "ACCOUNT_LOCKED",
  "msg": "Account locked due to too many failed attempts",
  "lockedUntil": "2024-01-15T12:30:00Z"
}
```

---

## Acceptance Criteria

- [ ] Can login with email
- [ ] Can login with phone
- [ ] Invalid credentials returns 401
- [ ] Locked account returns 403 with unlock time
- [ ] Successful login returns JWT tokens
- [ ] Access token expires in 1 hour
- [ ] lastLoginAt updated
- [ ] Login event logged with IP
