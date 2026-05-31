# PROD-010: List Products

## User Story
As a **customer**, I want to browse products with pagination and filters, so that I can find products easily.

## API
```
GET /api/v1/products
```

### Query Parameters
| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| page | Integer | No | 1 | Page number |
| size | Integer | No | 20 | Items per page (max 100) |
| categoryId | UUID | No | - | Filter by category |
| minPrice | Decimal | No | - | Minimum price filter |
| maxPrice | Decimal | No | - | Maximum price filter |
| status | String | No | ACTIVE | Product status filter |
| sellerId | UUID | No | - | Filter by seller |
| sortBy | String | No | createdAt | Sort field (price, name, createdAt, soldCount) |
| sortOrder | String | No | desc | Sort order (asc, desc) |

### Response (200 OK)
```json
{
  "success": true,
  "data": {
    "items": [
      {
        "id": "uuid-1",
        "name": "iPhone 15 Pro",
        "price": 999.99,
        "images": ["url1.jpg"],
        "status": "ACTIVE",
        "sellerId": "uuid-seller",
        "categoryId": "uuid-category",
        "viewCount": 1500,
        "soldCount": 50
      }
    ],
    "pagination": {
      "page": 1,
      "size": 20,
      "totalItems": 150,
      "totalPages": 8
    }
  }
}
```

## Acceptance Criteria
- [ ] Returns paginated list of products
- [ ] Only returns ACTIVE products to customers
- [ ] Supports filtering by category, price range, seller
- [ ] Supports sorting by price, name, createdAt, soldCount
- [ ] Only returns active products
- [ ] No authentication required
