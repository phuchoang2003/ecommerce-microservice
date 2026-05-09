# USER-003: Forgot Password

## User Story
> As a user who forgot my password, I can request a reset link so I can regain access to my account.

## API

### POST /api/v1/users/forgot-password

**Request:**
```json
{
  "email": "user@example.com"
}
```

**Response (200):**
```json
{
  "msg": "If the email exists, a reset link has been sent"
}
```

**Side Effects:**
- Generate reset token (30 min expiry)
- Send reset email with link containing token

---

## Acceptance Criteria

- [ ] Returns success msg even if email not found (security)
- [ ] Valid email triggers reset email send
- [ ] Reset token stored with 30 min expiry
- [ ] Kafka event `user.password-reset-requested` published
