# USER-033: Delete Address

## User Story
> As a logged-in user, I can delete an address so I can remove outdated or unwanted delivery locations.

## API

### DELETE /api/v1/users/me/addresses/{addressId}

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| addressId | UUID | The address ID to delete |

**Response (204):**
No content

**Business Rules:**
- Cannot delete the last address (must have at least 1 address)
- Cannot delete address if it is used in any pending orders

**Error Responses:**
- 400: Cannot delete last address
- 409: Address is used in pending orders
- 404: Address not found or does not belong to user

**Error Response (409 - Pending Orders):**
```json
{
  "error": "ADDRESS_IN_PENDING_ORDERS",
  "msg": "Cannot delete address that is used in pending orders",
  "pendingOrderIds": [
    "550e8400-e29b-41d4-a716-446655440100",
    "550e8400-e29b-41d4-a716-446655440101"
  ]
}
```

---

## Acceptance Criteria

- [ ] Can delete address not in pending orders
- [ ] 400 returned when attempting to delete last address
- [ ] 409 returned when address is used in pending orders
- [ ] Cannot delete another user's address
- [ ] 404 returned if address does not exist
- [ ] 401 returned if not authenticated
- [ ] If deleted address was default, next available address becomes default
