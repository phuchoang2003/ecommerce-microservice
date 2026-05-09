# SELL-012: Update Shop

## User Story

As a seller, I want to update my shop profile so that I can keep my business information current.

## API

**Endpoint:** `PUT /api/v1/seller/shops/me`
**Authentication:** Required (Bearer Token - Seller Role)

### Request Body

```json
{
  "name": "Acme Electronics Pte Ltd",
  "description": "Your trusted electronics supplier since 2020. Now with wider product range!",
  "address": "456 Business Park, Level 5, Singapore"
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| name | String | No | Updated shop name |
| description | String | No | Updated shop description |
| address | String | No | Updated shop address |

### Response (200 OK)

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "ownerId": "123e4567-e89b-12d3-a456-426614174000",
  "name": "Acme Electronics Pte Ltd",
  "description": "Your trusted electronics supplier since 2020. Now with wider product range!",
  "bannerUrl": "https://cdn.example.com/shops/550e8400/banner.jpg",
  "logoUrl": "https://cdn.example.com/shops/550e8400/logo.jpg",
  "address": "456 Business Park, Level 5, Singapore",
  "rating": 4.5,
  "status": "ACTIVE",
  "createdAt": "2026-01-15T08:00:00Z",
  "updatedAt": "2026-04-04T11:00:00Z"
}
```

### Error Responses

- `400 Bad Request` - Invalid input
- `401 Unauthorized` - Not authenticated
- `403 Forbidden` - User does not have permission to update shop
- `404 Not Found` - No shop found for user

## Acceptance Criteria

- [ ] Seller can update own shop details
- [ ] Only provided fields are updated (partial update)
- [ ] Shop name length is validated (3-100 characters)
- [ ] Description length is validated (max 500 characters)
- [ ] Address length is validated (max 200 characters)
- [ ] updatedAt timestamp is automatically set
- [ ] Only OWNER or MANAGER role can update