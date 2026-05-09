package com.hdp.notification_service.domain.valueobject;

import java.util.Map;

public record NotificationData(
        Map<String, Object> values
) {
}
