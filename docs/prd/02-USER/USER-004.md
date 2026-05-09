# USER-004: Reset Password

## User Story
> As a user with a reset token, I can set a new password so I can access my account again.

## API

### POST /api/v1/users/reset-password

**Request:**
```json
{
  "token": "reset-token-abc...",
  "newPassword": "NewSecurePass456"
}
```

**Validation:**
| Field | Rule |
|-------|------|
| token | Must be valid and not expired |
| newPassword | Min 8 chars, at least 1 letter, 1 number |

**Response (200):**
```json
{
  "success": true,
  "msg": "Password reset successfully"
}
```

**Side Effects:**
- Invalidate all existing sessions
- Clear all refresh tokens for this user
- Send password change notification email

---

## Acceptance Criteria

- [ ] Valid token allows password reset
- [ ] Invalid token returns 400
- [ ] Expired token returns 400
- [ ] New password must meet requirements
- [ ] All existing sessions invalidated
- [ ] Notification email sent
