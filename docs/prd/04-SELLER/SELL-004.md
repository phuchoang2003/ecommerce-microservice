# SELL-004: Cancel Application

## User Story

As a user, I want to cancel my pending seller application so that I can withdraw my request.

## API

**Endpoint:** `DELETE /api/v1/seller/application`
**Authentication:** Required (Bearer Token)

### Response (204 No Content)

No response body.

### Error Responses

- `401 Unauthorized` - Not authenticated
- `403 Forbidden` - Application is not in PENDING status
- `404 Not Found` - No application found for user

## Acceptance Criteria

- [ ] User can cancel only PENDING applications
- [ ] Cancellation is rejected for APPROVED applications
- [ ] Cancelled application is permanently deleted
- [ ] Returns 204 on success
- [ ] User can submit a new application after cancellation