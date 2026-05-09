# Logistics Service - PRD Documentation

## Overview
The Logistics Service handles shipping calculations, rate comparisons, tracking, label generation, and carrier webhook integrations for the e-commerce platform.

## User Stories

| Story ID | Title | Endpoint | Type |
|----------|-------|----------|------|
| [LOG-001](./LOG-001.md) | Calculate Shipping Fee | `/api/v1/logistics/shipping/calculate` | POST |
| [LOG-002](./LOG-002.md) | Get Shipping Rates | `/api/v1/logistics/shipping/rates` | GET |
| [LOG-003](./LOG-003.md) | Get Tracking | `/api/v1/logistics/tracking/{trackingNumber}` | GET |
| [LOG-004](./LOG-004.md) | Webhook Receiver | `/api/v1/logistics/webhook` | POST |
| [LOG-005](./LOG-005.md) | Report Issue | `/api/v1/logistics/tracking/{trackingNumber}/issue` | POST |
| [LOG-006](./LOG-006.md) | Get Delivery Proof | `/api/v1/logistics/tracking/{trackingNumber}/proof` | GET |
| [LOG-010](./LOG-010.md) | Generate Label | `/api/v1/logistics/labels` | POST |
| [LOG-011](./LOG-011.md) | Regenerate Label | `/api/v1/logistics/labels/{labelId}/regenerate` | POST |
| [LOG-012](./LOG-012.md) | Void Label | `/api/v1/logistics/labels/{labelId}/void` | POST |
| [LOG-013](./LOG-013.md) | Bulk Generate Labels | `/api/v1/logistics/labels/bulk` | POST |
