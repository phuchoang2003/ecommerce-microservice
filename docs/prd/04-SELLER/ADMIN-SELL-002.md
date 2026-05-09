# ADMIN-SELL-002: Reject Application

## User Story

As an admin, I want to reject seller applications so that I can reject fraudulent or incomplete applications.

## API

**Endpoint:** `PUT /api/v1/admin/seller-applications/{id}/reject`
**Authentication:** Required (Bearer Token - Admin Role)

### Path Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| id | UUID | Seller application unique identifier |

### Request Body

```json
{
  "reason": "Tax ID validation failed - number format invalid"
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| reason | String | Yes | Reason for rejection (max 500 characters) |

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
  "status": "REJECTED",
  "rejectionReason": "Tax ID validation failed - number format invalid",
  "submittedAt": "2026-04-04T10:30:00Z",
  "reviewedAt": "2026-04-04T14:00:00Z",
  "reviewedBy": "admin-uuid-123"
}
```

### Side Effects

- SellerApplication status changes to REJECTED
- Rejection reason is stored
- Notification email sent to applicant with reason

### Error Responses

- `400 Bad Request` - Reason is required and must be under 500 characters
- `401 Unauthorized` - Not authenticated
- `403 Forbidden` - User does not have admin role
- `404 Not Found` - Application not found
- `409 Conflict` - Application is not in PENDING status

## Acceptance Criteria

- [ ] Admin can reject pending applications only
- [ ] Rejection reason is required and recorded
- [ ] Applicant receives notification with reason
- [ ] Cannot reject already processed applications
- [ ] Audit trail includes reviewedBy and reviewedAt
- [ ] User can appeal or submit new application after 30 days