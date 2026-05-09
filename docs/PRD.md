# E-Commerce Platform PRD (Shopee Clone)

## 1. Project Overview

**Project Name:** Shopee-like E-Commerce Marketplace

**Core Functionality:** Multi-vendor online marketplace platform enabling sellers to list products, buyers to browse/purchase, with flash sales, promotions, reviews, and logistics integration.

**Target Users:**
- **Buyers:** Shop for products, compare prices, write reviews, track orders
- **Sellers:** Manage online shops, list products, process orders, handle fulfillment
- **Admins:** Platform oversight, seller approval, dispute resolution

---

## 2. User Stories

### 2.1 Buyer User Stories

| ID | Story |
|----|-------|
| **BUY-001** | As a buyer, I can register an account with email/phone so I can shop on the platform |
| **BUY-002** | As a buyer, I can search for products by keyword so I can find what I want |
| **BUY-003** | As a buyer, I can filter products by category, price range, rating, and location |
| **BUY-004** | As a buyer, I can view product details including photos, description, variants (size/color), and seller info |
| **BUY-005** | As a buyer, I can add products to my shopping cart so I can batch purchase |
| **BUY-006** | As a buyer, I can update item quantities or remove items from my cart |
| **BUY-007** | As a buyer, I can checkout with my cart, specifying shipping address and payment method |
| **BUY-008** | As a buyer, I can pay using credit card, bank transfer, or e-wallet |
| **BUY-009** | As a buyer, I can view my order history and track delivery status |
| **BUY-010** | As a buyer, I can cancel an order before it ships |
| **BUY-011** | As a buyer, I can rate and review products I purchased |
| **BUY-012** | As a buyer, I can save my favorite products to a wishlist |
| **BUY-013** | As a buyer, I can apply discount/coupon codes at checkout |
| **BUY-014** | As a buyer, I receive notifications (email/SMS/push) for order updates |

### 2.2 Seller User Stories

| ID | Story |
|----|-------|
| **SEL-001** | As a seller, I can register and apply for a seller account so I can open my shop |
| **SEL-002** | As a seller, I can set up my shop profile with banner, description, and contact info |
| **SEL-003** | As a seller, I can create product listings with photos, description, variants, and pricing |
| **SEL-004** | As a seller, I can manage my inventory (stock levels) |
| **SEL-005** | As a seller, I can process orders: accept, pack, and mark as shipped |
| **SEL-006** | As a seller, I can print shipping labels |
| **SEL-007** | As a seller, I can view sales analytics (orders, revenue, traffic) |
| **SEL-008** | As a seller, I can respond to buyer questions and reviews |
| **SEL-009** | As a seller, I can create flash sales and promotional campaigns |
| **SEL-010** | As a seller, I can set my own shipping fees or offer free shipping |

### 2.3 Admin User Stories

| ID | Story |
|----|-------|
| **ADM-001** | As an admin, I can approve/reject seller applications |
| **ADM-002** | As an admin, I can manage platform-wide categories |
| **ADM-003** | As an admin, I can create platform-wide promotions (discount codes, flash sales) |
| **ADM-004** | As an admin, I can handle buyer-seller disputes |
| **ADM-005** | As an admin, I can view platform metrics (GMV, orders, active users) |

---

## 3. Product Categories

### 3.1 Main Categories
- Electronics & Gadgets
- Fashion & Apparel
- Home & Living
- Health & Beauty
- Sports & Outdoors
- Groceries & Fresh
- Mother & Baby
- Automotive

### 3.2 Category Hierarchy
```
Electronics & Gadgets
├── Smartphones
│   ├── Android Phones
│   └── iOS Phones
├── Laptops
│   ├── Gaming Laptops
│   └── Business Laptops
└── Accessories
    ├── Headphones
    ├── Chargers
    └── Cases
```

---

## 4. Authorization & Permissions

### 4.1 User Roles

| Role | Description |
|------|-------------|
| **GUEST** | Unauthenticated user, can browse products, search |
| **BUYER** | Authenticated user, can purchase, write reviews |
| **SELLER** | Authenticated user with approved shop, can manage products/orders |
| **ADMIN** | Platform administrator, full system access |

### 4.2 Permission Matrix

| Action | GUEST | BUYER | SELLER | ADMIN |
|--------|-------|-------|--------|-------|
| Browse products | ✓ | ✓ | ✓ | ✓ |
| Search products | ✓ | ✓ | ✓ | ✓ |
| Add to cart | - | ✓ | ✓ | ✓ |
| Checkout | - | ✓ | ✓ | ✓ |
| Write reviews | - | ✓ | ✓ | ✓ |
| Create product listing | - | - | ✓ | ✓ |
| Manage own products | - | - | ✓ | ✓ |
| Process own orders | - | - | ✓ | ✓ |
| Approve seller applications | - | - | - | ✓ |
| Manage categories | - | - | - | ✓ |
| Platform promotions | - | - | - | ✓ |
| View all orders | - | - | - | ✓ |
| Handle disputes | - | - | - | ✓ |

### 4.3 Seller Sub-Permissions

| Permission | Shop Owner | Shop Staff |
|------------|------------|------------|
| Manage shop settings | ✓ | - |
| Add/edit products | ✓ | ✓ |
| Process orders | ✓ | ✓ |
| View analytics | ✓ | ✓ |
| Manage staff accounts | ✓ | - |

---

## 5. Business Rules

### 5.1 Order Management
- Order status flow: `PENDING` → `PAID` → `PROCESSING` → `SHIPPED` → `DELIVERED` → `COMPLETED`
- Buyer can cancel order only in `PENDING` or `PAID` status
- Order auto-cancels if payment not received within 30 minutes
- Each order can contain items from multiple sellers (multi-seller order)

### 5.2 Pricing & Payments
- Product price is set by seller; platform may add service fee
- Payment must be received before order processing
- Platform collects payment, holds in escrow, releases to seller after delivery
- Transaction fee: 5% deducted from seller payout

### 5.3 Inventory
- Stock is reserved when order is placed (not when added to cart)
- Reserved stock is released if payment not received within 30 minutes
- Overselling is prevented (real-time stock check before checkout)

### 5.4 Reviews
- Buyer can only review after order is delivered
- One review per product per order
- Review can include star rating (1-5) and text (up to 500 chars)
- Seller cannot delete reviews, only report inappropriate ones

### 5.5 Promotions
- Flash sales: Limited quantity, limited time, tiered pricing
- Coupons: Platform-wide or shop-specific, minimum purchase requirements
- Bundle deals: Buy X items, get discount on Y item

### 5.6 Seller Rules
- Seller must be approved before listing products
- Seller can have maximum 1 shop per account
- Seller rating calculated from: response time, description accuracy, shipping speed
- Sellers with rating below 3.0 may be suspended

---

## 6. Notifications

### 6.1 Notification Triggers

| Event | Buyer | Seller |
|-------|-------|--------|
| Order placed | ✓ | ✓ |
| Payment received | ✓ | ✓ |
| Order shipped | ✓ | - |
| Order delivered | ✓ | ✓ |
| Order completed | ✓ | - |
| New review received | - | ✓ |
| Flash sale starting | ✓ | ✓ |
| Low stock warning | - | ✓ |

### 6.2 Notification Channels
- In-app notification (always on)
- Email (opt-in)
- SMS (opt-in, may have costs)
- Push notification (mobile app)

---

## 7. Logistics

### 7.1 Shipping
- Seller chooses shipping carriers they work with
- Shipping fee calculated based on: weight, dimensions, destination
- Platform subsidizes shipping for flash sales/promotions

### 7.2 Tracking
- Real-time tracking from carrier APIs
- Status updates pushed to buyer via notifications

---

## 8. Search & Discovery

### 8.1 Search Features
- Full-text search on product name, description
- Autocomplete suggestions
- Search history

### 8.2 Filters
- Category (hierarchical)
- Price range (min-max slider)
- Rating (1-5 stars)
- Location (seller location)
- Shipping option (free shipping)
- Variant options (color, size)

### 8.3 Sorting
- Relevance
- Price (low to high, high to low)
- Sales count
- Rating
- Newest

---

## 9. Product Listing Requirements

### 9.1 Required Information
- Product name (max 200 chars)
- Category (must select from category tree)
- Price (in platform currency)
- Stock quantity
- At least 1 product photo (max 9)
- Product description (max 5000 chars)
- Product variants (optional): size, color, etc.

### 9.2 Prohibited Items
- Counterfeit goods
- Illegal items
- Hazardous materials
- Items requiring special licenses

---

## 10. Dispute Resolution

### 10.1 Buyer Complaints
- Item not received
- Item significantly different from description
- Item damaged/defective
- Wrong item delivered

### 10.2 Resolution Options
- Full refund
- Partial refund
- Reshipment
- No action (if complaint unfounded)

---

## 11. Success Metrics (KPIs)

| Metric | Description |
|--------|-------------|
| GMV | Gross Merchandise Value - total value of orders |
| Order Count | Number of successful orders |
| Active Buyers | Buyers who made at least 1 purchase in last 30 days |
| Active Sellers | Sellers with at least 1 product listed |
| Conversion Rate | (Orders / Page Views) × 100 |
| Average Order Value | GMV / Order Count |
| Customer Retention | Repeat purchase rate within 90 days |
