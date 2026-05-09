# Cart Service - User Stories

## Table of Contents

| ID | User Story | Endpoint | Auth |
|----|------------|----------|------|
| [CART-001](./CART-001.md) | View Cart | GET /api/v1/cart | Required |
| [CART-002](./CART-002.md) | Add Item | POST /api/v1/cart/items | Required |
| [CART-003](./CART-003.md) | Update Item | PUT /api/v1/cart/items/{itemId} | Required |
| [CART-004](./CART-004.md) | Remove Item | DELETE /api/v1/cart/items/{itemId} | Required |
| [CART-005](./CART-005.md) | Clear Cart | DELETE /api/v1/cart | Required |
| [CART-006](./CART-006.md) | Bulk Add | POST /api/v1/cart/items/bulk | Required |
| [CART-007](./CART-007.md) | Select Item | PUT /api/v1/cart/items/{itemId}/select | Required |
| [CART-008](./CART-008.md) | Deselect Item | PUT /api/v1/cart/items/{itemId}/deselect | Required |
| [CART-009](./CART-009.md) | Move to Wishlist | POST /api/v1/cart/items/{itemId}/wishlist | Required |
| [CART-010](./CART-010.md) | Cart Summary | GET /api/v1/cart/summary | Required |
| [CART-015](./CART-015.md) | Guest Create Cart | POST /api/v1/cart/guest | None |
| [CART-016](./CART-016.md) | Guest Add Item | POST /api/v1/cart/guest/{guestId}/items | None |
| [CART-017](./CART-017.md) | Guest Get Cart | GET /api/v1/cart/guest/{guestId} | None |
| [CART-018](./CART-018.md) | Merge Cart | POST /api/v1/cart/merge | Required |
| [CART-025](./CART-025.md) | Validate Cart | POST /api/v1/cart/validate | Required |
| [CART-026](./CART-026.md) | Validate Item | POST /api/v1/cart/items/validate | Required |
| [CART-027](./CART-027.md) | Remove Invalid | DELETE /api/v1/cart/invalid | Required |
| [CART-035](./CART-035.md) | Apply Coupon | POST /api/v1/cart/coupons | Required |
| [CART-036](./CART-036.md) | Remove Coupon | DELETE /api/v1/cart/coupons/{couponCode} | Required |
