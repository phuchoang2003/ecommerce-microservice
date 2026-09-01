package com.hdp.order_service.infrastructure.adapter.inbound.web.dto.response;

import com.hdp.order_service.domain.valueobject.OrderStatus;
import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.entity.OrderStatusHistoryJpa;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record OrderStatusHistoryResponse(
    UUID id,
    UUID orderId,
    OrderStatus previousStatus,
    OrderStatus newStatus,
    UUID changedBy,
    String reason,
    Instant createdAt
) {
    public static OrderStatusHistoryResponse fromEntity(OrderStatusHistoryJpa entity) {
        return OrderStatusHistoryResponse.builder()
            .id(entity.getId())
            .orderId(entity.getOrder() != null ? entity.getOrder().getId() : null)
            .previousStatus(entity.getPreviousStatus())
            .newStatus(entity.getNewStatus())
            .changedBy(entity.getChangedBy())
            .reason(entity.getReason())
            .createdAt(entity.getCreatedAt())
            .build();
    }
}
