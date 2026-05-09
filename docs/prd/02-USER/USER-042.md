# USER-042: Admin Delete User

## User Story
> As an admin, I can delete a user account so I can remove fraudulent or malicious accounts from the platform.

## API

### DELETE /api/v1/admin/users/{userId}

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| userId | UUID | The user ID to delete |

**Request:**
```json
{
  "reason": "Fraudulent account - fake identity"
}
```

**Validation:**
| Field | Rule |
|-------|------|
| reason | Required, non-empty string |

**Response (204):**
No content

**Business Rules:**
- Soft delete: user status is set to DELETED, data is retained
- Deleted users cannot login or be searched
- Deleted users' orders and addresses are retained for historical records

**Authorization:**
- Requires ADMIN role

**Side Effects:**
- All user's refresh tokens are revoked
- User status set to DELETED
- Publish `user.deleted` event to Kafka topic `user-events`

**Error Responses:**
- 400: Missing reason
- 404: User not found
- 409: User already deleted

---

## Acceptance Criteria

- [ ] Admin can delete user with reason
- [ ] User status is set to DELETED (soft delete)
- [ ] Deleted user cannot login
- [ ] Deleted user is excluded from admin list
- [ ] Reason is stored for audit purposes
- [ ] 400 returned for missing reason
- [ ] 404 returned if user does not exist
- [ ] 409 returned if user already DELETED
- [ ] 403 returned if not ADMIN role
- [ ] 401 returned if not authenticated
- [ ] Kafka event published on deletion
