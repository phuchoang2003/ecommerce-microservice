package com.hdp.notification_service.domain.valueobject;

import java.util.UUID;

public record DeliveryId(UUID values) {
    public static DeliveryId generate() {
        return new DeliveryId(UUID.randomUUID());
    }
}
