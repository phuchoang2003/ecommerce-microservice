# SELL-005: List Applications (Admin)

## User Story

As an admin, I want to list all seller applications so that I can review and process them.

## API

**Endpoint:** `GET /api/v1/admin/seller-applications`
**Authentication:** Required (Bearer Token - Admin Role)

### Query Parameters

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| status | String | No | all | Filter by status: PENDING, APPROVED, REJECTED |
| page | Integer | No | 1 | Page number |
| size | Integer | No | 20 | Items per page (max 100) |

### Response (200 OK)

```json
{
  "content": [
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
  ],
  "page": 1,
  "size": 20,
  "totalElements": 45,
  "totalPages": 3
}
```

### Error Responses

- `401 Unauthorized` - Not authenticated
- `403 Forbidden` - User does not have admin role

## Acceptance Criteria

- [ ] Admin can view all applications
- [ ] Admin can filter by status
- [ ] Results are paginated
- [ ] Applications are sorted by submittedAt (newest first)
- [ ] Only admin users can access this endpoint