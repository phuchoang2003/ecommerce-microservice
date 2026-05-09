package com.hdp.order_service.domain.model;

import com.hdp.order_service.domain.model.valueobject.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class OrderStatusHistory {
    private final UUID id;
    private final OrderStatus previousStatus;
    private final OrderStatus newStatus;
    private final UUID changedBy;
    private final String reason;
    private final Instant createdAt;

    @Builder
    public OrderStatusHistory(UUID id, OrderStatus previousStatus, OrderStatus newStatus,
                              UUID changedBy, String reason, Instant createdAt) {
        this.id = id;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.changedBy = changedBy;
        this.reason = reason;
        this.createdAt = createdAt;
    }

    public static OrderStatusHistory create(OrderStatus previousStatus, OrderStatus newStatus,
                                            UUID changedBy, String reason) {
        return OrderStatusHistory.builder()
                .previousStatus(previousStatus)
                .newStatus(newStatus)
                .changedBy(changedBy)
                .reason(reason)
                .createdAt(Instant.now())
                .build();
    }
}