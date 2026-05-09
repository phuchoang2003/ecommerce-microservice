# CART-010: Cart Summary

## User Story

As a **logged-in customer**, I want to **view a summary of my cart** so that I can **quickly see selected items and total cost before checkout**.

## API

```
GET /api/v1/cart/summary
Authorization: Bearer {token}
```

### Response Example

```json
{
  "cartId": "cart-uuid-12345",
  "selectedItemsCount": 3,
  "selectedItemsTotal": 149.97,
  "items": [
    {
      "itemId": "item-uuid-11111",
      "productName": "Wireless Mouse",
      "variantName": "Black",
      "quantity": 2,
      "subtotal": 59.98
    },
    {
      "itemId": "item-uuid-22222",
      "productName": "USB Keyboard",
      "variantName": "Mechanical",
      "quantity": 1,
      "subtotal": 89.99
    }
  ],
  "couponCode": "SAVE10",
  "discountAmount": 14.99,
  "finalTotal": 134.98
}
```

## Acceptance Criteria

- [ ] Returns only selected items (not deselected items)
- [ ] Shows count of selected items
- [ ] Shows subtotal of selected items
- [ ] Shows applied coupon code and discount if present
- [ ] Shows final total after discount
- [ ] Returns 404 if user has no cart
- [ ] Returns 401 if not authenticated
- [ ] Returns empty summary if no items are selected
