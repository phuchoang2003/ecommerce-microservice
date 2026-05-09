# User Service

**Port:** 8081  
**Database:** MySQL

## User Stories

| ID | Feature | File |
|----|---------|------|
| USER-001 | Register account | [USER-001.md](./USER-001.md) |
| USER-002 | Verify email | [USER-002.md](./USER-002.md) |
| USER-003 | Forgot password | [USER-003.md](./USER-003.md) |
| USER-004 | Reset password | [USER-004.md](./USER-004.md) |
| USER-010 | Login | [USER-010.md](./USER-010.md) |
| USER-011 | Refresh token | [USER-011.md](./USER-011.md) |
| USER-012 | Logout | [USER-012.md](./USER-012.md) |
| USER-020 | View profile | [USER-020.md](./USER-020.md) |
| USER-021 | Update profile | [USER-021.md](./USER-021.md) |
| USER-022 | Change password | [USER-022.md](./USER-022.md) |
| USER-030 | List addresses | [USER-030.md](./USER-030.md) |
| USER-031 | Add address | [USER-031.md](./USER-031.md) |
| USER-032 | Update address | [USER-032.md](./USER-032.md) |
| USER-033 | Delete address | [USER-033.md](./USER-033.md) |
| USER-040 | Admin - List users | [USER-040.md](./USER-040.md) |
| USER-041 | Admin - Update status | [USER-041.md](./USER-041.md) |
| USER-042 | Admin - Delete user | [USER-042.md](./USER-042.md) |

## Data Models

### User
```
- id: UUID
- email: String (unique)
- phone: String (unique)
- passwordHash: String
- role: BUYER | SELLER
- status: PENDING_VERIFICATION | ACTIVE | BANNED | DELETED
- avatarUrl: String?
- createdAt: Timestamp
- updatedAt: Timestamp
- lastLoginAt: Timestamp?
```

### Address
```
- id: UUID
- userId: UUID
- province: String
- district: String
- ward: String
- street: String
- isDefault: Boolean
- createdAt: Timestamp
```

## Business Rules

- Max 10 addresses per user
- First address is automatically default
- Cannot delete address used in pending orders
- Password min 8 chars, must contain letter and number
- Rate limit: 5 failed login attempts → lock 15 min
- Refresh token TTL: 7 days
- Access token TTL: 1 hour
