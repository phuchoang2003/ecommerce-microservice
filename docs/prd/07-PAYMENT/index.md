# Payment Service - PRD Documentation

## Table of User Stories

| ID | Title | Endpoint | Priority |
|----|-------|----------|----------|
| [PAY-001](./PAY-001.md) | Create Payment Intent | POST /api/v1/payments/intent | P0 |
| [PAY-002](./PAY-002.md) | Get Payment | GET /api/v1/payments/{paymentId} | P0 |
| [PAY-003](./PAY-003.md) | Get Payment Intent | GET /api/v1/payments/intent/{intentId} | P0 |
| [PAY-004](./PAY-004.md) | Cancel Payment | POST /api/v1/payments/{paymentId}/cancel | P1 |
| [PAY-005](./PAY-005.md) | List Payments | GET /api/v1/payments | P1 |
| [PAY-010](./PAY-010.md) | Webhook Handler | POST /api/v1/payments/webhook | P0 |
| [PAY-020](./PAY-020.md) | Create Refund | POST /api/v1/payments/{paymentId}/refund | P1 |
| [PAY-021](./PAY-021.md) | Partial Refund | POST /api/v1/payments/{paymentId}/refund | P1 |
| [PAY-022](./PAY-022.md) | List Refunds | GET /api/v1/payments/{paymentId}/refunds | P2 |
| [PAY-030](./PAY-030.md) | Get Escrow | GET /api/v1/payments/escrow/{orderId} | P1 |
| [PAY-031](./PAY-031.md) | Manual Release | POST /api/v1/admin/escrow/release | P2 |
