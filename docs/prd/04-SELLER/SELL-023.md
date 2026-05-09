# SELL-023: Remove Staff

## User Story

As a seller (owner/manager), I want to remove a staff member from my shop so that I can manage team access.

## API

**Endpoint:** `DELETE /api/v1/seller/staff/{staffId}`
**Authentication:** Required (Bearer Token - Seller Role)

### Path Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| staffId | UUID | Staff member unique identifier |

### Response (204 No Content)

No response body.

### Error Responses

- `401 Unauthorized` - Not authenticated
- `403 Forbidden` - User does not have permission
- `404 Not Found` - Staff member not found in shop
- `409 Conflict` - Cannot remove OWNER

## Acceptance Criteria

- [ ] Owner can remove any staff member
- [ ] Manager can remove STAFF only (not other MANAGER)
- [ ] Cannot remove OWNER from the shop
- [ ] Staff record is soft-deleted (status set to INACTIVE)
- [ ] Removed staff loses access immediately
- [ ] Notification is sent to the removed staff member
- [ ] Can be re-invited in the future