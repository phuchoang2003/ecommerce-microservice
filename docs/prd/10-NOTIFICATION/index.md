# Notification Service

**Port:** 8089
**Database:** MySQL

## Features

| File | Description |
|------|-------------|
| [channels.md](./channels.md) | Notification channels setup |
| [triggers.md](./triggers.md) | Event triggers and templates |
| [in-app.md](./in-app.md) | In-app notifications |
| [preferences.md](./preferences.md) | User notification preferences |

## Overview

The Notification Service handles multi-channel notifications across Email (SendGrid), SMS (Twilio), Push (FCM), and In-App channels. It is event-driven via Kafka and supports notification templates with user preference management.

## Data Models

### Notification
```
- id: UUID
- userId: UUID
- channel: EMAIL | SMS | PUSH | IN_APP
- type: String
- title: String
- content: String
- data: JSON
- isRead: Boolean
- sentAt: Timestamp
- readAt: Timestamp?
```

### UserPreference
```
- id: UUID
- userId: UUID
- channel: EMAIL | SMS | PUSH | IN_APP
- type: String
- enabled: Boolean
- createdAt: Timestamp
- updatedAt: Timestamp
```

### NotificationTemplate
```
- id: UUID
- code: String (unique)
- channel: EMAIL | SMS | PUSH | IN_APP
- eventType: String
- titleTemplate: String
- contentTemplate: String
- createdAt: Timestamp
- updatedAt: Timestamp
```

## User Stories

| ID | Title | Endpoint | Method |
|----|-------|----------|--------|
| [NOT-001](./NOT-001.md) | Register Device | `/api/v1/notifications/devices` | POST |
| [NOT-002](./NOT-002.md) | List Devices | `/api/v1/notifications/devices` | GET |
| [NOT-003](./NOT-003.md) | Delete Device | `/api/v1/notifications/devices/{deviceId}` | DELETE |
| [NOT-010](./NOT-010.md) | Get Notifications | `/api/v1/notifications` | GET |
| [NOT-011](./NOT-011.md) | Mark Read | `/api/v1/notifications/{notificationId}/read` | PUT |
| [NOT-012](./NOT-012.md) | Mark All Read | `/api/v1/notifications/read-all` | PUT |
| [NOT-013](./NOT-013.md) | Delete Notification | `/api/v1/notifications/{notificationId}` | DELETE |
| [NOT-014](./NOT-014.md) | Get Unread Count | `/api/v1/notifications/unread-count` | GET |
| [NOT-015](./NOT-015.md) | Update Preferences | `/api/v1/notifications/preferences` | PUT |
| [NOT-016](./NOT-016.md) | Get Preferences | `/api/v1/notifications/preferences` | GET |

## Supported Channels

| Channel | Provider | Opt-in | Notes |
|---------|----------|--------|-------|
| IN_APP | DB | Always on | Stored in database |
| EMAIL | SendGrid | Opt-in | May have costs at scale |
| SMS | Twilio | Opt-in | Costs per msg |
| PUSH | FCM | Opt-in | Mobile app only |

## Kafka Topics

| Topic | Direction | Description |
|-------|-----------|-------------|
| `notification.order` | Consumer | Order events |
| `notification.payment` | Consumer | Payment events |
| `notification.review` | Consumer | Review events |
| `notification.promotion` | Consumer | Promotion events |
| `notification.outbound` | Producer | Outbound notifications |

## Business Rules

- In-app notifications are always enabled and cannot be disabled
- All paid channels (Email, SMS) require explicit opt-in
- Push notifications require a valid device token
- Notification history is retained for 90 days
- Users can opt-out per channel and per notification type
- Rate limits apply per channel (configurable per user)
