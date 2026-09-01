package com.hdp.order_service.application.port.in.updatesuborderstatus;

import com.hdp.order_service.domain.valueobject.SubOrderStatus;

import java.util.UUID;

public record UpdateSubOrderStatusCommand(UUID id, SubOrderStatus status, UUID changedBy, String reason) {
}
