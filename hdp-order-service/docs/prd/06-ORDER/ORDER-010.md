# ORDER-010: List Orders - Buyer Views Their Orders with Pagination and Filter

## User Story

**As a** buyer
**I want to** view my orders with pagination and status filter
**So that** I can easily find and manage my past purchases

---

## API

**Endpoint:** `GET /api/v1/orders`

### Request Headers
| Header | Value |
|--------|-------|
| Authorization | Bearer {buyer_token} |

### Query Parameters
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| page | int | 0 | Page number (0-indexed) |
| size | int | 10 | Items per page (max 50) |
| status | string | - | Filter by status (PENDING, PAID, PROCESSING, SHIPPED, DELIVERED, COMPLETED, CANCELLED) |
| sort | string | createdAt,desc | Sort field and direction |

### Response (200 OK)
```json
{
  "content": [
    {
      "orderId": "uuid",
      "orderNumber": "ORD-20240404-XXXX",
      "status": "SHIPPED",
      "subtotal": 150000.00,
      "shippingFee": 25000.00,
      "discount": 15000.00,
      "tax": 14000.00,
      "totalAmount": 174000.00,
      "itemCount": 3,
      "createdAt": "2024-04-04T10:00:00Z",
      "updatedAt": "2024-04-04T14:30:00Z"
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 25,
  "totalPages": 3
}
```

### Error Responses
| Status | Code | Description |
|--------|------|-------------|
| 400 | INVALID_STATUS | Status filter value is not valid |
| 400 | INVALID_PAGE | Page number is negative |

---

## Acceptance Criteria

- [ ] Buyer can view only their own orders
- [ ] Results are paginated with configurable page size
- [ ] Status filter works correctly (single status)
- [ ] Results are sorted by createdAt descending by default
- [ ] Response includes total count for pagination
- [ ] Order summary shows key information (order number, status, total, item count)
