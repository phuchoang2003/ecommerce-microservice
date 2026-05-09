# USER-031: Add Address

## User Story
> As a logged-in user, I can add a new address so I can save delivery locations for future orders.

## API

### POST /api/v1/users/me/addresses

**Request:**
```json
{
  "province": "Ho Chi Minh City",
  "district": "District 7",
  "ward": "Tan Hung Ward",
  "street": "789 Nguyen Van Linh Street",
  "isDefault": false
}
```

**Validation:**
| Field | Rule |
|-------|------|
| province | Required, non-empty string |
| district | Required, non-empty string |
| ward | Required, non-empty string |
| street | Required, non-empty string |
| isDefault | Optional, boolean (default: false) |

**Response (201):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440003",
  "province": "Ho Chi Minh City",
  "district": "District 7",
  "ward": "Tan Hung Ward",
  "street": "789 Nguyen Van Linh Street",
  "isDefault": false,
  "createdAt": "2024-01-25T09:00:00Z"
}
```

**Business Rules:**
- Maximum 10 addresses per user
- First address added is automatically set as default
- If isDefault=true, unset previous default address

**Side Effects:**
- If first address, automatically set as default
- If isDefault=true, previous default address is set to false

**Error Responses:**
- 400: Validation failed
- 409: Maximum 10 addresses reached

---

## Acceptance Criteria

- [ ] Can add address with valid fields
- [ ] First address is automatically set as default
- [ ] Can explicitly set isDefault=true on new address
- [ ] Setting isDefault=true unsets previous default
- [ ] 400 returned for missing required fields
- [ ] 409 returned when user already has 10 addresses
- [ ] 401 returned if not authenticated
