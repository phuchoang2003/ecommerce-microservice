# SELL-003: Update Application

## User Story

As a user, I want to update my pending seller application so that I can correct any errors before review.

## API

**Endpoint:** `PUT /api/v1/seller/application`
**Authentication:** Required (Bearer Token)

### Request Body

```json
{
  "businessName": "Acme Electronics Pte Ltd",
  "businessType": "COMPANY",
  "taxId": "1234567890123",
  "bankAccount": "9876543210",
  "bankName": "Central Bank"
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| businessName | String | No | Updated legal business name |
| businessType | Enum | No | INDIVIDUAL or COMPANY |
| taxId | String | No | Updated tax identification number |
| bankAccount | String | No | Updated bank account number |
| bankName | String | No | Updated bank name |

### Response (200 OK)

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "userId": "123e4567-e89b-12d3-a456-426614174000",
  "businessName": "Acme Electronics Pte Ltd",
  "businessType": "COMPANY",
  "taxId": "1234567890123",
  "bankAccount": "9876543210",
  "bankName": "Central Bank",
  "status": "PENDING",
  "submittedAt": "2026-04-04T10:30:00Z"
}
```

### Error Responses

- `400 Bad Request` - Invalid input format
- `401 Unauthorized` - Not authenticated
- `403 Forbidden` - Application is not in PENDING status
- `404 Not Found` - No application found for user

## Acceptance Criteria

- [ ] User can update only PENDING applications
- [ ] Updates are rejected for APPROVED or REJECTED applications
- [ ] Only fields provided in request are updated (partial update)
- [ ] Updated timestamp is recorded
- [ ] All field validations are applied