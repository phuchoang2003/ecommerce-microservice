# Search Service

**Port:** 8090
**Database:** Elasticsearch

## User Stories

| ID | Title | Endpoint |
|----|-------|----------|
| SCH-001 | Search Products | GET /api/v1/search?q=iphone&filters=... |
| SCH-002 | Search Suggestions | GET /api/v1/search/suggestions?q=iph |
| SCH-003 | Get Filters | GET /api/v1/search/filters |
| SCH-004 | Get Trending | GET /api/v1/search/trending |
| SCH-005 | Get Recent Searches | GET /api/v1/search/recent |
| SCH-006 | Clear Search History | DELETE /api/v1/search/history |
| SCH-010 | Get Recommendations | GET /api/v1/search/recommendations |
| SCH-011 | Get Similar | GET /api/v1/search/similar/{productId} |
| SCH-012 | Get Trending Products | GET /api/v1/search/trending-products |
| SCH-013 | Get New Arrivals | GET /api/v1/search/new-arrivals |

## Data Models

### Product Document
```
- productId: String
- name: String (boosted 3x in search)
- description: String (boosted 1x)
- categoryId: String
- categoryPath: String[] (hierarchical)
- sellerId: String
- sellerName: String
- price: Decimal
- originalPrice: Decimal?
- images: String[]
- rating: Float (1-5)
- soldCount: Integer
- location: String
- freeShipping: Boolean
- variants: String[]
- createdAt: Timestamp
```

## Search Features

### Full-Text Search
- Query on `name` (boosted 3x) and `description` (boosted 1x)
- Elasticsearch query string with relevance scoring
- Minimum score threshold for quality results

### Autocomplete
- Prefix matching on product name
- Maximum 10 suggestions
- Return productId, name, and image

### Filters
- **Category**: Hierarchical path matching (e.g., electronics > phones > smartphones)
- **Price Range**: Min/max bounds
- **Rating**: 1-5 star minimum
- **Location**: Seller location match
- **Free Shipping**: Boolean filter
- **Variants**: Variant name matching

### Sorting
- **relevance** (default): Elasticsearch relevance score
- **price_asc**: Price low to high
- **price_desc**: Price high to low
- **sales_count**: Best sellers first
- **rating**: Highest rated first
- **newest**: Most recently created first

### Recommendations
- Personalized based on user browsing history
- Purchase history influence
- Similar products to liked items
