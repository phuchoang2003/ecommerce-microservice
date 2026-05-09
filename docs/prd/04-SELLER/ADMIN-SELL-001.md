# ADMIN-SELL-001: Approve Application

## User Story

As an admin, I want to approve seller applications so that users can start selling on the platform.

## API

**Endpoint:** `PUT /api/v1/admin/seller-applications/{id}/approve`
**Authentication:** Required (Bearer Token - Admin Role)

### Path Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| id | UUID | Seller application unique identifier |

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
  "status": "APPROVED",
  "submittedAt": "2026-04-04T10:30:00Z",
  "reviewedAt": "2026-04-04T14:00:00Z",
  "reviewedBy": "admin-uuid-123"
}
```

### Side Effects

- SellerApplication status changes to APPROVED
- New Shop record is automatically created
- User is granted SELLER role
- Notification email sent to applicant

### Error Responses

- `401 Unauthorized` - Not authenticated
- `403 Forbidden` - User does not have admin role
- `404 Not Found` - Application not found
- `409 Conflict` - Application is not in PENDING status

## Acceptance Criteria

- [ ] Admin can approve pending applications only
- [ ] Shop is automatically created upon approval
- [ ] Applicant receives notification
- [ ] KYC data is stored with the shop
- [ ] Cannot approve already processed applications
- [ ] Audit trail includes reviewedBy and reviewedAt