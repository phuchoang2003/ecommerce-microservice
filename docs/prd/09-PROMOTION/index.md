# Promotion Service

**Port:** 8087
**Cache:** Redis
**Database:** MySQL

## User Stories

| ID | User Story | Endpoint | Method |
|----|-----------|----------|--------|
| PRO-001 | Create Coupon | /api/v1/promotions/coupons | POST |
| PRO-002 | List Coupons | /api/v1/promotions/coupons | GET |
| PRO-003 | Update Coupon | /api/v1/promotions/coupons/{couponId} | PUT |
| PRO-004 | Deactivate Coupon | /api/v1/promotions/coupons/{couponId}/deactivate | PUT |
| PRO-005 | Validate Coupon | /api/v1/promotions/coupons/validate | POST |
| PRO-006 | Apply Coupon | /api/v1/promotions/coupons/apply | POST |
| PRO-007 | Create Flash Sale | /api/v1/promotions/flash-sales | POST |
| PRO-008 | Add Flash Sale Product | /api/v1/promotions/flash-sales/{id}/products | POST |
| PRO-009 | Start Flash Sale | /api/v1/promotions/flash-sales/{id}/start | PUT |
| PRO-010 | End Flash Sale | /api/v1/promotions/flash-sales/{id}/end | PUT |
| PRO-011 | Create Bundle Deal | /api/v1/promotions/bundles | POST |
| PRO-012 | Validate Bundle | /api/v1/promotions/bundles/validate | POST |
| PRO-013 | Apply Bundle | /api/v1/promotions/bundles/apply | POST |
| PRO-014 | Free Shipping Promo | /api/v1/promotions/free-shipping | POST |
| PRO-015 | Calculate Shipping | /api/v1/promotions/free-shipping/calculate | GET |

## Features

| File | Description |
|------|-------------|
| [coupon.md](./coupon.md) | Coupon codes: platform-wide and shop-specific |
| [flash-sale.md](./flash-sale.md) | Flash sales: limited quantity, limited time, tiered pricing |
| [bundle-deal.md](./bundle-deal.md) | Bundle deals: buy X get Y discount |
| [free-shipping.md](./free-shipping.md) | Free shipping promotions |

## Data Models

### Coupon
```
- id: UUID
- code: String (unique)
- type: PLATFORM | SHOP
- shopId: UUID? (required for SHOP type)
- discountType: PERCENTAGE | FIXED
- discountValue: Decimal
- minPurchase: Decimal
- maxDiscount: Decimal? (cap for percentage discounts)
- startDate: Timestamp
- endDate: Timestamp
- usageLimit: Integer?
- usedCount: Integer
- applicableProducts: UUID[]? (empty = all products)
- applicableCategories: UUID[]? (empty = all categories)
- status: ACTIVE | INACTIVE | EXPIRED
- createdAt: Timestamp
- updatedAt: Timestamp
```

### FlashSale
```
- id: UUID
- name: String
- startTime: Timestamp
- endTime: Timestamp
- products: ProductItem[]
- status: SCHEDULED | ACTIVE | ENDED
- createdAt: Timestamp
```

### ProductItem (FlashSale)
```
- productId: UUID
- originalPrice: Decimal
- salePrice: Decimal
- stock: Integer
- soldCount: Integer
```

### BundleDeal
```
- id: UUID
- name: String
- products: UUID[]
- buyQuantity: Integer
- getDiscount: DiscountItem[]
- maxBundles: Integer?
- usedCount: Integer
- startDate: Timestamp
- endDate: Timestamp
- status: ACTIVE | INACTIVE | EXPIRED
- createdAt: Timestamp
```

### DiscountItem (BundleDeal)
```
- type: PERCENTAGE | FIXED
- value: Decimal
```

### FreeShippingPromotion
```
- id: UUID
- name: String
- type: PLATFORM | SHOP
- shopId: UUID? (required for SHOP type)
- minPurchase: Decimal
- maxFreeShippingAmount: Decimal?
- applicableAreas: String[]? (empty = all areas)
- startDate: Timestamp
- endDate: Timestamp
- usageLimit: Integer?
- usedCount: Integer
- status: ACTIVE | INACTIVE | EXPIRED
- createdAt: Timestamp
```

## Business Rules

| Rule | Description |
|------|-------------|
| Coupon min purchase | Purchase must meet minimum amount to apply coupon |
| Coupon max discount cap | Percentage discounts capped at maxDiscount value |
| Flash sale stock | Limited stock tracked per product, soldCount cannot exceed stock |
| Flash sale price | Sale price must be lower than original price |
| Bundle discount | Buy X get Y applied to cheapest qualifying item in bundle |
| Free shipping scope | Platform-wide or shop-specific |

## Validation Rules

| Entity | Field | Rule |
|--------|-------|------|
| Coupon | code | 3-30 characters, alphanumeric and dash only |
| Coupon | discountValue | Must be positive |
| Coupon | minPurchase | Must be >= 0 |
| Coupon | maxDiscount | Must be >= discountValue if percentage |
| FlashSale | salePrice | Must be < originalPrice |
| FlashSale | stock | Must be >= 0 |
| BundleDeal | buyQuantity | Must be >= 2 |
| BundleDeal | getDiscount | Must be > 0 and <= 100 if percentage |
| FreeShipping | minPurchase | Must be >= 0 |
