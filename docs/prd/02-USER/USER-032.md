# USER-032: Update Address

## User Story
> As a logged-in user, I can update my saved addresses so I can keep my delivery information current.

## API

### PUT /api/v1/users/me/addresses/{addressId}

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| addressId | UUID | The address ID to update |

**Request:**
```json
{
  "province": "Ho Chi Minh City",
  "district": "District 3",
  "ward": "Ward 4",
  "street": "321 Le Loi Street",
  "isDefault": true
}
```

**Validation:**
| Field | Rule |
|-------|------|
| province | Optional, non-empty string if provided |
| district | Optional, non-empty string if provided |
| ward | Optional, non-empty string if provided |
| street | Optional, non-empty string if provided |
| isDefault | Optional, boolean |

**Response (200):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440001",
  "province": "Ho Chi Minh City",
  "district": "District 3",
  "ward": "Ward 4",
  "street": "321 Le Loi Street",
  "isDefault": true,
  "createdAt": "2024-01-15T10:30:00Z"
}
```

**Business Rules:**
- Partial update: only provided fields are updated
- If isDefault=true, unset previous default address

**Side Effects:**
- If isDefault=true, previous default address is set to false

**Error Responses:**
- 400: Validation failed
- 404: Address not found or does not belong to user

---

## Acceptance Criteria

- [ ] Can update any single field (partial update)
- [ ] Can update multiple fields at once
- [ ] Setting isDefault=true unsets previous default
- [ ] Cannot update another user's address
- [ ] 404 returned if address does not exist
- [ ] 400 returned for invalid field values
- [ ] 401 returned if not authenticated
