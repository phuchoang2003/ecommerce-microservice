package com.hdp.notification_service.domain.model;

import com.hdp.notification_service.domain.valueobject.NotificationChannel;
import com.hdp.notification_service.domain.valueobject.NotificationData;
import com.hdp.notification_service.domain.valueobject.NotificationId;
import com.hdp.notification_service.domain.valueobject.NotificationType;
import com.hdp.notification_service.domain.valueobject.UserId;

import java.time.Instant;

public class Notification {
    private NotificationId id;
    private UserId userId;
    private NotificationData data;
    private NotificationType type;
    private boolean isRead;
    private Instant createdAt;

    public void markAsRead() {
        this.isRead = true;
    }
}
