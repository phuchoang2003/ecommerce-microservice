# USER-022: Change Password

## User Story
> As a logged in user, I can change my password so my account stays secure.

## API

### PUT /api/v1/users/me/password

**Request:**
```json
{
  "currentPassword": "OldSecurePass123",
  "newPassword": "NewSecurePass456"
}
```

**Validation:**
| Field | Rule |
|-------|------|
| currentPassword | Must match current password |
| newPassword | Min 8 chars, at least 1 letter, 1 number, cannot equal current |

**Response (200):**
```json
{
  "success": true,
  "msg": "Password changed successfully"
}
```

**Side Effects:**
- Invalidate all existing refresh tokens (force re-login)
- Send password change notification email

---

## Acceptance Criteria

- [ ] Current password required
- [ ] Wrong current password returns 400
- [ ] New password must meet requirements
- [ ] New password cannot equal current
- [ ] All sessions invalidated
- [ ] Notification email sent
