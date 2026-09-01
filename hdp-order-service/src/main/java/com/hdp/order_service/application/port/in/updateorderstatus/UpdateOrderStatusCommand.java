package com.hdp.order_service.application.port.in.updateorderstatus;

import com.hdp.order_service.domain.valueobject.OrderStatus;

import java.util.UUID;

public record UpdateOrderStatusCommand(UUID id, OrderStatus status, UUID changedBy, String reason) {
}
