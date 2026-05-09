# USER-002: Verify Email

## User Story
> As a new user, I can verify my email so my account becomes active.

## API

### POST /api/v1/users/verify-email

**Request:**
```json
{
  "token": "abc123xyz..."
}
```

**Response (200):**
```json
{
  "success": true,
  "msg": "Email verified successfully"
}
```

**Status Transition:** `PENDING_VERIFICATION` → `ACTIVE`

---

## Acceptance Criteria

- [ ] Valid token activates account
- [ ] Invalid token returns 400
- [ ] Expired token returns 400
- [ ] Already verified token returns 400
- [ ] Status changes to ACTIVE
- [ ] Kafka event `user.email-verified` published
