# SELL-001: Apply Seller

## User Story

As a user, I want to submit a seller application with KYC information so that I can start selling on the platform.

## API

**Endpoint:** `POST /api/v1/seller/apply`
**Authentication:** Required (Bearer Token)

### Request Body

```json
{
  "businessName": "Acme Electronics",
  "businessType": "COMPANY",
  "taxId": "1234567890123",
  "bankAccount": "9876543210",
  "bankName": "Central Bank"
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| businessName | String | Yes | Legal business name |
| businessType | Enum | Yes | INDIVIDUAL or COMPANY |
| taxId | String | Yes | Tax identification number |
| bankAccount | String | Yes | Bank account number for settlements |
| bankName | String | Yes | Name of the bank |

### Response (201 Created)

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
  "submittedAt": "2026-04-04T10:30:00Z"
}
```

### Error Responses

- `400 Bad Request` - Invalid input or missing required fields
- `401 Unauthorized` - Not authenticated
- `409 Conflict` - User already has an application (pending or approved)

## Acceptance Criteria

- [ ] User can submit application with all required KYC fields
- [ ] Application status is set to PENDING upon submission
- [ ] Duplicate applications (pending/approved) are rejected with 409
- [ ] Tax ID format is validated
- [ ] Bank account number is validated
- [ ] User receives confirmation with application ID after submission