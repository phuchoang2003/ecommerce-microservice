# CART-001: View Cart

## User Story

As a **logged-in customer**, I want to **view my shopping cart** so that I can **see all items I have added before proceeding to checkout**.

## API

```
GET /api/v1/cart
Authorization: Bearer {token}
```

### Response Example

```json
{
  "cartId": "cart-uuid-12345",
  "userId": "user-uuid-67890",
  "items": [
    {
      "itemId": "item-uuid-11111",
      "productId": "prod-uuid-001",
      "variantId": "var-uuid-001",
      "productName": "Wireless Mouse",
      "variantName": "Black",
      "quantity": 2,
      "unitPrice": 29.99,
      "subtotal": 59.98,
      "selected": true,
      "addedAt": "2026-04-01T10:30:00Z"
    }
  ],
  "totalItems": 2,
  "totalAmount": 59.98,
  "createdAt": "2026-04-01T10:00:00Z",
  "updatedAt": "2026-04-01T10:30:00Z"
}
```

## Acceptance Criteria

- [ ] Returns cart with all items for authenticated user
- [ ] Returns empty cart if user has no items
- [ ] Returns 401 if not authenticated
- [ ] Includes item details (product, variant, quantity, price)
- [ ] Shows total items count and total amount
- [ ] Indicates which items are selected for checkout
