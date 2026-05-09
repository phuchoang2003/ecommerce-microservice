# Seller Service - User Stories Index

**Service Port:** 8086
**Database:** PostgreSQL

## Table of Contents

### Seller Application (SELL-001 to SELL-005)
| ID | Title | Endpoint |
|----|-------|----------|
| [SELL-001](./SELL-001.md) | Apply Seller | POST /api/v1/seller/apply |
| [SELL-002](./SELL-002.md) | Check Application Status | GET /api/v1/seller/application/status |
| [SELL-003](./SELL-003.md) | Update Application | PUT /api/v1/seller/application |
| [SELL-004](./SELL-004.md) | Cancel Application | DELETE /api/v1/seller/application |
| [SELL-005](./SELL-005.md) | List Applications (Admin) | GET /api/v1/admin/seller-applications |

### Shop Management (SELL-010 to SELL-015)
| ID | Title | Endpoint |
|----|-------|----------|
| [SELL-010](./SELL-010.md) | View Shop | GET /api/v1/shops/{shopId} |
| [SELL-011](./SELL-011.md) | View My Shop | GET /api/v1/seller/shops/me |
| [SELL-012](./SELL-012.md) | Update Shop | PUT /api/v1/seller/shops/me |
| [SELL-013](./SELL-013.md) | Upload Banner | POST /api/v1/seller/shops/me/banner |
| [SELL-014](./SELL-014.md) | Upload Logo | POST /api/v1/seller/shops/me/logo |
| [SELL-015](./SELL-015.md) | Close Shop | PUT /api/v1/seller/shops/me/close |

### Staff Management (SELL-020 to SELL-023)
| ID | Title | Endpoint |
|----|-------|----------|
| [SELL-020](./SELL-020.md) | Invite Staff | POST /api/v1/seller/staff/invite |
| [SELL-021](./SELL-021.md) | List Staff | GET /api/v1/seller/staff |
| [SELL-022](./SELL-022.md) | Update Staff Role | PUT /api/v1/seller/staff/{staffId}/role |
| [SELL-023](./SELL-023.md) | Remove Staff | DELETE /api/v1/seller/staff/{staffId} |

### Analytics (SELL-030 to SELL-033)
| ID | Title | Endpoint |
|----|-------|----------|
| [SELL-030](./SELL-030.md) | Sales Analytics | GET /api/v1/seller/analytics/sales |
| [SELL-031](./SELL-031.md) | Traffic Analytics | GET /api/v1/seller/analytics/traffic |
| [SELL-032](./SELL-032.md) | Rating Analytics | GET /api/v1/seller/analytics/ratings |
| [SELL-033](./SELL-033.md) | Overview Dashboard | GET /api/v1/seller/analytics/overview |

### Admin Actions (ADMIN-SELL-001 to ADMIN-SELL-002)
| ID | Title | Endpoint |
|----|-------|----------|
| [ADMIN-SELL-001](./ADMIN-SELL-001.md) | Approve Application | PUT /api/v1/admin/seller-applications/{id}/approve |
| [ADMIN-SELL-002](./ADMIN-SELL-002.md) | Reject Application | PUT /api/v1/admin/seller-applications/{id}/reject |

## Data Models

### SellerApplication
```
- id: UUID
- userId: UUID
- businessName: String
- businessType: INDIVIDUAL | COMPANY
- taxId: String
- bankAccount: String
- bankName: String
- status: PENDING | APPROVED | REJECTED
- rejectionReason: String?
- submittedAt: Timestamp
- reviewedAt: Timestamp?
- reviewedBy: UUID?
```

### Shop
```
- id: UUID
- ownerId: UUID
- name: String
- description: String?
- bannerUrl: String?
- logoUrl: String?
- address: String
- rating: Decimal
- status: ACTIVE | INACTIVE | SUSPENDED
- suspendedAt: Timestamp?
- createdAt: Timestamp
- updatedAt: Timestamp
```

### ShopStaff
```
- id: UUID
- shopId: UUID
- userId: UUID
- role: OWNER | MANAGER | STAFF
- status: ACTIVE | INACTIVE
- invitedAt: Timestamp
- joinedAt: Timestamp?
```

## Rating Calculation

Seller rating is calculated based on buyer feedback:
- Response Time: 30%
- Accuracy: 40%
- Shipping Speed: 30%

**Suspension Rule:** If average rating falls below 3.0 for 30 consecutive days, the seller shop will be suspended.