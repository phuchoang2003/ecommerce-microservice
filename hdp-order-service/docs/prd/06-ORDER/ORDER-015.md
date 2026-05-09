# ORDER-015: List Seller Orders - Seller Views Their Orders

## User Story

**As a** seller
**I want to** view all orders containing my products
**So that** I can manage and fulfill customer orders

---

## API

**Endpoint:** `GET /api/v1/seller/orders`

### Request Headers
| Header | Value |
|--------|-------|
| Authorization | Bearer {seller_token} |

### Query Parameters
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| page | int | 0 | Page number (0-indexed) |
| size | int | 10 | Items per page (max 50) |
| status | string | - | Filter by sub-order status |
| dateFrom | ISO date | - | Filter orders from date |
| dateTo | ISO date | - | Filter orders to date |
| sort | string | createdAt,desc | Sort field and direction |

### Response (200 OK)
```json
{
  "content": [
    {
      "subOrderId": "uuid",
      "orderId": "uuid",
      "orderNumber": "ORD-20240404-XXXX",
      "status": "PAID",
      "buyerName": "John Doe",
      "itemCount": 3,
      "totalAmount": 50000.00,
      "createdAt": "2024-04-04T10:00:00Z"
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 50,
  "totalPages": 5
}
```

### Error Responses
| Status | Code | Description |
|--------|------|-------------|
| 400 | INVALID_STATUS | Status filter value is not valid |

---

## Acceptance Criteria

- [ ] Seller can view only orders containing their products
- [ ] Results are filtered by authenticated seller
- [ ] Results show sub-orders, not main orders (one per seller)
- [ ] Pagination works correctly
- [ ] Status filter works for sub-order statuses
- [ ] Date range filter works correctly
- [ ] Results are sorted by createdAt descending by default
