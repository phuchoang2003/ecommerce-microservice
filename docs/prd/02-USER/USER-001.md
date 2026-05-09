# USER-001: Register Account

## User Story
> As a guest, I can register an account with email/phone and password so I can become a buyer on the platform.

## API

### POST /api/v1/users/register

**Request:**
```json
{
  "email": "user@example.com",
  "phone": "0901234567",
  "password": "SecurePass123",
  "role": "BUYER"
}
```

**Validation:**
| Field | Rule |
|-------|------|
| email | Valid email format, unique in DB |
| phone | Vietnam format (10-11 digits), unique in DB |
| password | Min 8 chars, at least 1 letter, 1 number |
| role | Must be "BUYER" or "SELLER" |

**Response (201):**
```json
{
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "email": "user@example.com",
  "role": "BUYER",
  "status": "PENDING_VERIFICATION"
}
```

**Side Effects:**
- Send verification email to user
- Publish `user.registered` event to Kafka topic `user-events`

---

## Acceptance Criteria

- [ ] Can register with valid email, phone, password
- [ ] Duplicate email returns 409
- [ ] Invalid email format returns 400
- [ ] Invalid phone format returns 400
- [ ] Weak password returns 400
- [ ] Cannot register as ADMIN role
- [ ] Verification email sent
- [ ] Kafka event published
