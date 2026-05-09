# ORDER-030: Admin List Orders - Admin Views All Orders

## User Story

**As an** admin
**I want to** view all orders in the system
**So that** I can monitor and manage the platform's order activity

---

## API

**Endpoint:** `GET /api/v1/admin/orders`

### Request Headers
| Header | Value |
|--------|-------|
| Authorization | Bearer {admin_token} |

### Query Parameters
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| page | int | 0 | Page number (0-indexed) |
| size | int | 20 | Items per page (max 100) |
| status | string | - | Filter by order status |
| sellerId | UUID | - | Filter by seller |
| buyerId | UUID | - | Filter by buyer |
| dateFrom | ISO date | - | Filter orders from date |
| dateTo | ISO date | - | Filter orders to date |
| search | string | - | Search by order number |
| sort | string | createdAt,desc | Sort field and direction |

### Response (200 OK)
```json
{
  "content": [
    {
      "orderId": "uuid",
      "orderNumber": "ORD-20240404-XXXX",
      "buyerId": "uuid",
      "buyerName": "John Doe",
      "status": "COMPLETED",
      "subtotal": 150000.00,
      "totalAmount": 174000.00,
      "itemCount": 3,
      "sellerCount": 2,
      "createdAt": "2024-04-04T10:00:00Z",
      "updatedAt": "2024-04-04T18:00:00Z"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 500,
  "totalPages": 25
}
```

### Error Responses
| Status | Code | Description |
|--------|------|-------------|
| 400 | INVALID_STATUS | Status filter value is not valid |
| 400 | INVALID_DATE_RANGE | Date from is after date to |

---

## Acceptance Criteria

- [ ] Admin can view all orders across all sellers and buyers
- [ ] Advanced filtering by status, seller, buyer, and date range
- [ ] Search by order number is supported
- [ ] Pagination supports up to 100 items per page
- [ ] Results include summary of sub-orders (seller count)
- [ ] Results are sorted by createdAt descending by default
