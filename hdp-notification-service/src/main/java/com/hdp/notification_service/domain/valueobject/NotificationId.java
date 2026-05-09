package com.hdp.notification_service.domain.valueobject;

import java.util.UUID;

public record NotificationId(UUID value) {
    public static NotificationId generate() {
        return new NotificationId(UUID.randomUUID());
    }
}
