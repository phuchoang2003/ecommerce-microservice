package com.hdp.order_service.application.port.in.cancelorder;

import java.util.UUID;

public record CancelOrderCommand(UUID id, UUID cancelledBy, String reason) {
}
