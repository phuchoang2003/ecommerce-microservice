# SELL-014: Upload Logo

## User Story

As a seller, I want to upload a logo image for my shop so that I can establish my brand identity.

## API

**Endpoint:** `POST /api/v1/seller/shops/me/logo`
**Authentication:** Required (Bearer Token - Seller Role)

### Request

Content-Type: `multipart/form-data`

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| file | File | Yes | Logo image (JPEG, PNG; max 2MB; recommended 200x200px) |

### Response (200 OK)

```json
{
  "logoUrl": "https://cdn.example.com/shops/550e8400/logo.jpg"
}
```

### Error Responses

- `400 Bad Request` - Invalid file format or size
- `401 Unauthorized` - Not authenticated
- `403 Forbidden` - User does not have permission
- `404 Not Found` - No shop found for user
- `413 Payload Too Large` - File exceeds 2MB

## Acceptance Criteria

- [ ] Seller can upload logo image for own shop
- [ ] Only JPEG and PNG formats are accepted
- [ ] Maximum file size is 2MB
- [ ] Image is automatically resized to optimal dimensions
- [ ] Returns URL of uploaded logo
- [ ] Previous logo is replaced
- [ ] Only OWNER or MANAGER role can upload