package com.hdp.notification_service.domain.model;

import com.hdp.notification_service.domain.valueobject.DeliveryId;
import com.hdp.notification_service.domain.valueobject.DeliveryStatus;
import com.hdp.notification_service.domain.valueobject.NotificationChannel;
import com.hdp.notification_service.domain.valueobject.NotificationId;

import java.time.Instant;

public class NotificationDelivery {
    private DeliveryId id;
    private NotificationId notificationId;
    private NotificationChannel channel;
    private DeliveryStatus status;
    private Instant sentAt;
}
