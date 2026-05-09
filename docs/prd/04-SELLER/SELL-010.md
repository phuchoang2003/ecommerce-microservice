# SELL-010: View Shop

## User Story

As a visitor, I want to view a shop's public profile so that I can learn about the seller before purchasing.

## API

**Endpoint:** `GET /api/v1/shops/{shopId}`
**Authentication:** Not required (Public)

### Path Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| shopId | UUID | Shop unique identifier |

### Response (200 OK)

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Acme Electronics",
  "description": "Your trusted electronics supplier since 2020",
  "bannerUrl": "https://cdn.example.com/shops/550e8400/banner.jpg",
  "logoUrl": "https://cdn.example.com/shops/550e8400/logo.jpg",
  "address": "123 Commerce Street, Singapore",
  "rating": 4.5,
  "status": "ACTIVE",
  "createdAt": "2026-01-15T08:00:00Z"
}
```

### Error Responses

- `404 Not Found` - Shop does not exist or is not active

## Acceptance Criteria

- [ ] Public endpoint accessible without authentication
- [ ] Returns shop details including rating and status
- [ ] Returns 404 for non-existent shops
- [ ] Only ACTIVE shops are viewable by public
- [ ] Banner and logo URLs are included if available