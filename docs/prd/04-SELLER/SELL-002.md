# SELL-002: Check Application Status

## User Story

As a user, I want to check the status of my seller application so that I know if it has been reviewed.

## API

**Endpoint:** `GET /api/v1/seller/application/status`
**Authentication:** Required (Bearer Token)

### Response (200 OK)

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "userId": "123e4567-e89b-12d3-a456-426614174000",
  "businessName": "Acme Electronics",
  "businessType": "COMPANY",
  "taxId": "1234567890123",
  "bankAccount": "9876543210",
  "bankName": "Central Bank",
  "status": "PENDING",
  "submittedAt": "2026-04-04T10:30:00Z",
  "rejectionReason": null
}
```

### Status Values

| Status | Description |
|--------|-------------|
| PENDING | Application is under review |
| APPROVED | Application approved, shop created |
| REJECTED | Application rejected with reason |

### Error Responses

- `401 Unauthorized` - Not authenticated
- `404 Not Found` - No application found for user

## Acceptance Criteria

- [ ] User can view their application status
- [ ] Response includes all application details
- [ ] Rejection reason is shown if application was rejected
- [ ] Returns 404 if no application exists