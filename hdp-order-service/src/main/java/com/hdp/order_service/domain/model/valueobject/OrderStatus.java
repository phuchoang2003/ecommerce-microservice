package com.hdp.order_service.domain.model.valueobject;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public enum OrderStatus {
    PENDING,
    PAID,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    COMPLETED,
    CANCELLED,
    PAYMENT_FAILED;

    private static final Map<OrderStatus, Set<OrderStatus>> VALID_TRANSITIONS = Map.of(
        PENDING, EnumSet.of(PAID, CANCELLED, PAYMENT_FAILED),
        PAID, EnumSet.of(PROCESSING, CANCELLED),
        PROCESSING, EnumSet.of(SHIPPED, CANCELLED),
        SHIPPED, EnumSet.of(DELIVERED),
        DELIVERED, EnumSet.of(COMPLETED),
        PAYMENT_FAILED, EnumSet.of(PAID, CANCELLED),
        COMPLETED, EnumSet.noneOf(OrderStatus.class),
        CANCELLED, EnumSet.noneOf(OrderStatus.class)
    );

    public boolean canTransitionTo(OrderStatus target) {
        Set<OrderStatus> validTargets = VALID_TRANSITIONS.get(this);
        return validTargets != null && validTargets.contains(target);
    }

    public boolean isCancellable() {
        return this == PENDING || this == PAID || this == PROCESSING;
    }

    public boolean isTerminal() {
        return this == COMPLETED || this == CANCELLED;
    }
}