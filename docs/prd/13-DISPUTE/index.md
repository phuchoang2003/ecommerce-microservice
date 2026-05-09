# Dispute Service - User Stories Index

| ID | Title | Actor | Endpoint |
|----|-------|-------|----------|
| DSP-001 | Create Dispute | Buyer | POST /api/v1/disputes |
| DSP-002 | List My Disputes | Buyer | GET /api/v1/disputes |
| DSP-003 | Get Dispute | Buyer/Seller | GET /api/v1/disputes/{disputeId} |
| DSP-004 | Add Evidence | Buyer/Seller | POST /api/v1/disputes/{disputeId}/evidence |
| DSP-005 | Cancel Dispute | Buyer | DELETE /api/v1/disputes/{disputeId} |
| DSP-010 | Seller Respond | Seller | PUT /api/v1/disputes/{disputeId}/respond |
| DSP-011 | Seller Propose Resolution | Seller | PUT /api/v1/disputes/{disputeId}/propose-resolution |
| DSP-012 | Seller Request Extension | Seller | PUT /api/v1/disputes/{disputeId}/request-extension |
| DSP-020 | Admin List Disputes | Admin | GET /api/v1/admin/disputes |
| DSP-021 | Admin Get Dispute | Admin | GET /api/v1/admin/disputes/{disputeId} |
| DSP-022 | Admin Escalate Dispute | Admin | PUT /api/v1/admin/disputes/{disputeId}/escalate |
| DSP-023 | Admin Resolve Dispute | Admin | PUT /api/v1/admin/disputes/{disputeId}/resolve |
| DSP-024 | Process Refund | Admin | POST /api/v1/admin/disputes/{disputeId}/refund |
