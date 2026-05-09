# Product Service - User Stories Index

## Categories (PROD-001 to PROD-005)

| ID | User Story | API Endpoint | Priority |
|-----|------------|--------------|----------|
| PROD-001 | List Categories | GET /api/v1/categories | Must Have |
| PROD-002 | Get Category | GET /api/v1/categories/{categoryId} | Must Have |
| PROD-003 | Create Category | POST /api/v1/admin/categories | Must Have |
| PROD-004 | Update Category | PUT /api/v1/admin/categories/{categoryId} | Must Have |
| PROD-005 | Delete Category | DELETE /api/v1/admin/categories/{categoryId} | Must Have |

## Products (PROD-010 to PROD-015)

| ID | User Story | API Endpoint | Priority |
|-----|------------|--------------|----------|
| PROD-010 | List Products | GET /api/v1/products | Must Have |
| PROD-011 | Get Product | GET /api/v1/products/{productId} | Must Have |
| PROD-012 | Create Product | POST /api/v1/seller/products | Must Have |
| PROD-013 | Update Product | PUT /api/v1/seller/products/{productId} | Must Have |
| PROD-014 | Update Status | PUT /api/v1/seller/products/{productId}/status | Must Have |
| PROD-015 | Upload Images | POST /api/v1/seller/products/{productId}/images | Should Have |

## Inventory (PROD-020 to PROD-026)

| ID | User Story | API Endpoint | Priority |
|-----|------------|--------------|----------|
| PROD-020 | View Inventory | GET /api/v1/seller/products/{productId}/inventory | Must Have |
| PROD-021 | Update Stock | PUT /api/v1/seller/inventory/{variantId} | Must Have |
| PROD-022 | Bulk Update | POST /api/v1/seller/inventory/bulk | Should Have |
| PROD-023 | Low Stock Alert | GET /api/v1/seller/inventory/low-stock | Must Have |
| PROD-024 | Reserve Inventory (internal) | POST /internal/inventory/reserve | Must Have |
| PROD-025 | Release Reservation (internal) | POST /internal/inventory/release | Must Have |
| PROD-026 | Commit Reservation (internal) | POST /internal/inventory/commit | Must Have |

## Admin (PROD-030 to PROD-031)

| ID | User Story | API Endpoint | Priority |
|-----|------------|--------------|----------|
| PROD-030 | Admin List Products | GET /api/v1/admin/products | Must Have |
| PROD-031 | Admin Update Status | PUT /api/v1/admin/products/{productId}/status | Must Have |
