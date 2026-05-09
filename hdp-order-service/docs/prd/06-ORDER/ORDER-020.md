# ORDER-020: Print Label - Seller Generates Shipping Label

## User Story

**As a** seller
**I want to** generate a shipping label for an order
**So that** I can print and attach it to my package

---

## API

**Endpoint:** `GET /api/v1/seller/orders/{orderId}/label`

### Request Headers
| Header | Value |
|--------|-------|
| Authorization | Bearer {seller_token} |

### Path Parameters
| Parameter | Type | Description |
|-----------|------|-------------|
| orderId | UUID | Sub-order identifier |

### Query Parameters
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| format | string | PDF | Label format (PDF, ZPL, PNG) |
| size | string | NORMAL | Label size (NORMAL, SMALL, A4) |

### Response (200 OK)

**Content-Type:** application/pdf (or image/png, text/plain for ZPL)

Binary label data is returned directly.

### Response Headers
| Header | Value |
|--------|-------|
| Content-Disposition | attachment; filename="label-{orderNumber}.pdf" |
| Content-Type | application/pdf |

### Error Responses
| Status | Code | Description |
|--------|------|-------------|
| 404 | ORDER_NOT_FOUND | Sub-order does not exist |
| 403 | ACCESS_DENIED | Seller does not own this sub-order |
| 400 | ORDER_NOT_SHIPPED | Order must be in SHIPPED status to print label |
| 400 | CARRIER_NOT_SUPPORTED | Carrier does not support label generation |

---

## Acceptance Criteria

- [ ] Seller can generate label for shipped orders
- [ ] Label includes shipping address, tracking number, and barcode
- [ ] Multiple formats are supported (PDF, ZPL, PNG)
- [ ] Seller can only generate labels for their own sub-orders
- [ ] Label filename includes order number for easy identification
