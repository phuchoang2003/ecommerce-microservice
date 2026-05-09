# CART-006: Bulk Add Items

## User Story

As a **logged-in customer**, I want to **add multiple items to my cart at once** so that I can **quickly populate my cart from a wishlist or product list**.

## API

```
POST /api/v1/cart/items/bulk
Authorization: Bearer {token}
Content-Type: application/json

{
  "items": [
    {
      "productId": "prod-uuid-001",
      "variantId": "var-uuid-001",
      "quantity": 2
    },
    {
      "productId": "prod-uuid-002",
      "variantId": "var-uuid-002",
      "quantity": 1
    }
  ]
}
```

### Response Example

```json
{
  "addedItems": [
    {
      "itemId": "item-uuid-33333",
      "productId": "prod-uuid-001",
      "variantId": "var-uuid-001",
      "quantity": 2,
      "unitPrice": 29.99,
      "subtotal": 59.98
    },
    {
      "itemId": "item-uuid-44444",
      "productId": "prod-uuid-002",
      "variantId": "var-uuid-002",
      "quantity": 1,
      "unitPrice": 49.99,
      "subtotal": 49.99
    }
  ],
  "failedItems": [],
  "totalAdded": 2
}
```

## Acceptance Criteria

- [ ] Adds all valid items to cart in a single operation
- [ ] Returns 400 if items array is empty
- [ ] Returns 400 if any item has invalid productId, variantId, or quantity
- [ ] Returns 401 if not authenticated
- [ ] Continues adding valid items even if some items fail validation
- [ ] Reports failed items with reasons
- [ ] Merges quantities if item already exists in cart
