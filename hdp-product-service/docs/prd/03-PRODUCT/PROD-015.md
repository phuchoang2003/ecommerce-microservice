# PROD-015: Upload Product Images

## User Story
As a **seller**, I want to upload product images, so that customers can see what they are buying.

## API
```
POST /api/v1/seller/products/{productId}/images
```

### Headers
| Header | Value | Required |
|--------|-------|----------|
| Authorization | Bearer {token} | Yes |
| Content-Type | multipart/form-data | Yes |

### Path Parameters
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| productId | UUID | Yes | Product unique identifier |

### Form Data
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| files | File[] | Yes | Image files (JPEG, PNG, WebP) |

### Constraints
- Maximum 9 images per product
- Maximum file size: 5MB per image
- Supported formats: JPEG, PNG, WebP

### Response (200 OK)
```json
{
  "success": true,
  "data": {
    "images": [
      "https://storage.example.com/products/uuid-1/img1.jpg",
      "https://storage.example.com/products/uuid-1/img2.jpg"
    ],
    "totalImages": 2
  }
}
```

### Error Responses
- **400 Bad Request**: Invalid file type or too many images
- **401 Unauthorized**: Not authenticated
- **403 Forbidden**: Not the product owner
- **404 Not Found**: Product does not exist

## Acceptance Criteria
- [ ] Seller can only upload images to their own products
- [ ] Maximum 9 images per product enforced
- [ ] Only JPEG, PNG, WebP formats accepted
- [ ] Maximum 5MB per image enforced
- [ ] Images are stored and URLs are returned
- [ ] First image is considered the primary image
