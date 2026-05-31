# PROD-001: List Categories

## User Story
As a **customer**, I want to browse categories in a hierarchical tree structure, so that I can easily navigate the product catalog.

## API
```
GET /api/v1/categories
```

### Response (200 OK)
```json
{
  "success": true,
  "data": [
    {
      "id": "uuid-1",
      "name": "Electronics",
      "level": 1,
      "children": [
        {
          "id": "uuid-2",
          "name": "Smartphones",
          "level": 2,
          "children": [
            {
              "id": "uuid-3",
              "name": "Android Phones",
              "level": 3,
              "children": []
            }
          ]
        }
      ]
    }
  ]
}
```

## Acceptance Criteria
- [ ] Returns hierarchical tree structure with parent-child relationships
- [ ] Only returns categories up to 3 levels deep (L1, L2, L3)
- [ ] Categories are sorted alphabetically within each level
- [ ] Empty children arrays are returned for leaf categories
- [ ] No authentication required
