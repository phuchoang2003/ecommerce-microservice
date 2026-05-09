# Order Service

**Port:** 8084
**Database:** PostgreSQL

## User Stories

| ID | File | Description |
|----|------|-------------|
| ORDER-001 | [ORDER-001.md](ORDER-001.md) | Checkout: POST /api/v1/checkout (reserve inventory, create payment intent) |
| ORDER-002 | [ORDER-002.md](ORDER-002.md) | Checkout with Coupon: POST /api/v1/checkout with couponCodes |
| ORDER-010 | [ORDER-010.md](ORDER-010.md) | List Orders: GET /api/v1/orders with pagination and status filter |
| ORDER-011 | [ORDER-011.md](ORDER-011.md) | Get Order: GET /api/v1/orders/{orderId} |
| ORDER-012 | [ORDER-012.md](ORDER-012.md) | Cancel Order: POST /api/v1/orders/{orderId}/cancel (only PENDING/PAID) |
| ORDER-013 | [ORDER-013.md](ORDER-013.md) | Track Order: GET /api/v1/orders/{orderId}/tracking |
| ORDER-014 | [ORDER-014.md](ORDER-014.md) | Confirm Payment: POST /api/v1/orders/{orderId}/confirm-payment (webhook) |
| ORDER-015 | [ORDER-015.md](ORDER-015.md) | List Seller Orders: GET /api/v1/seller/orders |
| ORDER-016 | [ORDER-016.md](ORDER-016.md) | Get Seller Order: GET /api/v1/seller/orders/{orderId} |
| ORDER-017 | [ORDER-017.md](ORDER-017.md) | Process Order: PUT /api/v1/seller/orders/{orderId}/process (PAID to PROCESSING) |
| ORDER-018 | [ORDER-018.md](ORDER-018.md) | Ship Order: PUT /api/v1/seller/orders/{orderId}/ship {trackingNumber, carrier} |
| ORDER-019 | [ORDER-019.md](ORDER-019.md) | Bulk Ship: POST /api/v1/seller/orders/bulk-ship |
| ORDER-020 | [ORDER-020.md](ORDER-020.md) | Print Label: GET /api/v1/seller/orders/{orderId}/label |
| ORDER-030 | [ORDER-030.md](ORDER-030.md) | Admin List Orders: GET /api/v1/admin/orders |
| ORDER-031 | [ORDER-031.md](ORDER-031.md) | Admin Update Status: PUT /api/v1/admin/orders/{orderId}/status |
| ORDER-032 | [ORDER-032.md](ORDER-032.md) | Admin Export: GET /api/v1/admin/orders/export |

## Order Status Flow

```
PENDING -> PAID -> PROCESSING -> SHIPPED -> DELIVERED -> COMPLETED
    |         |         |           |
    v         v         v           v
CANCELLED PAYMENT_FAILED CANCELLED  CANCELLED
```

## Sub-Order Status Flow

```
PAID -> PROCESSING -> SHIPPED -> DELIVERED -> COMPLETED
         |              |
         v              v
      CANCELLED      CANCELLED
```

## Data Models

### Order
```
- id: UUID
- orderNumber: String (unique, auto-generated)
- buyerId: UUID
- shippingAddressId: UUID
- paymentMethod: CREDIT_CARD | BANK_TRANSFER | E_WALLET | COD
- status: PENDING | PAID | PROCESSING | SHIPPED | DELIVERED | COMPLETED | CANCELLED | PAYMENT_FAILED
- subtotal: Decimal
- shippingFee: Decimal
- discount: Decimal
- tax: Decimal
- totalAmount: Decimal
- expiresAt: Timestamp (30 min from creation for PENDING)
- createdAt: Timestamp
- updatedAt: Timestamp
```

### OrderItem
```
- id: UUID
- orderId: UUID
- subOrderId: UUID
- sellerId: UUID
- productId: UUID
- variantId: UUID
- productName: String (snapshot)
- variantName: String (snapshot)
- price: Decimal (snapshot)
- quantity: Integer
```

### SubOrder
```
- id: UUID
- orderId: UUID
- sellerId: UUID
- status: PAID | PROCESSING | SHIPPED | DELIVERED | COMPLETED | CANCELLED
- trackingNumber: String?
- carrier: String?
- estimatedDelivery: Date?
- createdAt: Timestamp
- updatedAt: Timestamp
```

### PaymentMethod
| Value | Description |
|-------|-------------|
| CREDIT_CARD | Visa, Mastercard, etc. |
| BANK_TRANSFER | Direct bank transfer |
| E_WALLET | Digital wallet (MoMo, ZaloPay, etc.) |
| COD | Cash on delivery |

## Business Rules

- Multi-seller orders create a parent Order with multiple SubOrders (one per seller)
- Each SubOrder tracks fulfillment independently
- Main Order status is computed from SubOrder statuses
- PENDING orders auto-cancel after 30 minutes without payment
- Buyer can only cancel orders in PENDING or PAID status
- Sellers can only cancel their SubOrder in PROCESSING status (with refund)
