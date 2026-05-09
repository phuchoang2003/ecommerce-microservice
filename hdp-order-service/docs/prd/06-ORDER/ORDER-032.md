# ORDER-032: Admin Export Orders - Admin Exports Orders to File

## User Story

**As an** admin
**I want to** export orders to a file
**So that** I can perform offline analysis or generate reports

---

## API

**Endpoint:** `GET /api/v1/admin/orders/export`

### Request Headers
| Header | Value |
|--------|-------|
| Authorization | Bearer {admin_token} |

### Query Parameters
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| format | string | CSV | Export format (CSV, EXCEL) |
| status | string | - | Filter by order status |
| sellerId | UUID | - | Filter by seller |
| buyerId | UUID | - | Filter by buyer |
| dateFrom | ISO date | - | Filter orders from date |
| dateTo | ISO date | - | Filter orders to date |
| fields | string | - | Comma-separated list of fields to include |

### Response (200 OK)

**Content-Type:** text/csv (or application/vnd.openxmlformats-officedocument.spreadsheetml.sheet for Excel)

```
orderId,orderNumber,buyerName,status,totalAmount,itemCount,sellerCount,createdAt,updatedAt
uuid,ORD-20240404-XXXX,John Doe,COMPLETED,174000.00,3,2,2024-04-04T10:00:00Z,2024-04-04T18:00:00Z
```

### Response Headers
| Header | Value |
|--------|-------|
| Content-Disposition | attachment; filename="orders-export-20240404.csv" |
| Content-Type | text/csv |

### Error Responses
| Status | Code | Description |
|--------|------|-------------|
| 400 | NO_ORDERS_FOUND | No orders match the filter criteria |
| 400 | EXPORT_TOO_LARGE | Export exceeds maximum size (100,000 orders) |
| 400 | INVALID_FORMAT | Export format is not supported |

---

## Business Rules

- Maximum 100,000 orders per export
- Filters are applied before export
- Default fields: orderId, orderNumber, buyerName, status, totalAmount, createdAt
- CSV uses UTF-8 encoding with BOM for Excel compatibility

---

## Acceptance Criteria

- [ ] Admin can export all orders with applied filters
- [ ] Export supports CSV and Excel formats
- [ ] Maximum 100,000 orders per export
- [ ] Custom fields can be specified
- [ ] Filters are applied to exported data
- [ ] Filename includes export date
- [ ] Large exports are processed asynchronously for better performance
