package com.hdp.order_service.application.port.in.getorderhistory;

import com.hdp.order_service.domain.valueobject.OrderStatus;

import java.time.Instant;
import java.util.UUID;

public record OrderStatusHistoryView(
    UUID id, UUID orderId, OrderStatus previousStatus, OrderStatus newStatus,
    UUID changedBy, String reason, Instant createdAt
) {
}
