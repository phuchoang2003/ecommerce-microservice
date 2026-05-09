# SELL-013: Upload Banner

## User Story

As a seller, I want to upload a banner image for my shop so that I can customize my shop's appearance.

## API

**Endpoint:** `POST /api/v1/seller/shops/me/banner`
**Authentication:** Required (Bearer Token - Seller Role)

### Request

Content-Type: `multipart/form-data`

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| file | File | Yes | Banner image (JPEG, PNG; max 5MB; recommended 1200x400px) |

### Response (200 OK)

```json
{
  "bannerUrl": "https://cdn.example.com/shops/550e8400/banner.jpg"
}
```

### Error Responses

- `400 Bad Request` - Invalid file format or size
- `401 Unauthorized` - Not authenticated
- `403 Forbidden` - User does not have permission
- `404 Not Found` - No shop found for user
- `413 Payload Too Large` - File exceeds 5MB

## Acceptance Criteria

- [ ] Seller can upload banner image for own shop
- [ ] Only JPEG and PNG formats are accepted
- [ ] Maximum file size is 5MB
- [ ] Image is automatically resized/cropped to optimal dimensions
- [ ] Returns URL of uploaded banner
- [ ] Previous banner is replaced
- [ ] Only OWNER or MANAGER role can upload