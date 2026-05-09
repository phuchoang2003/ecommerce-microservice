# SELL-011: View My Shop

## User Story

As a seller, I want to view my own shop details so that I can see how it appears to customers.

## API

**Endpoint:** `GET /api/v1/seller/shops/me`
**Authentication:** Required (Bearer Token - Seller Role)

### Response (200 OK)

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "ownerId": "123e4567-e89b-12d3-a456-426614174000",
  "name": "Acme Electronics",
  "description": "Your trusted electronics supplier since 2020",
  "bannerUrl": "https://cdn.example.com/shops/550e8400/banner.jpg",
  "logoUrl": "https://cdn.example.com/shops/550e8400/logo.jpg",
  "address": "456 Business Park, Singapore",
  "rating": 4.5,
  "status": "ACTIVE",
  "suspendedAt": null,
  "createdAt": "2026-01-15T08:00:00Z",
  "updatedAt": "2026-04-01T14:30:00Z"
}
```

### Error Responses

- `401 Unauthorized` - Not authenticated
- `403 Forbidden` - User is not a seller with approved application
- `404 Not Found` - No shop found for user

## Acceptance Criteria

- [ ] Seller can view their own shop details
- [ ] Response includes all shop metadata
- [ ] Seller can see suspension status if applicable
- [ ] Returns 404 if user has no shop
- [ ] Only OWNER or staff with appropriate permissions can access