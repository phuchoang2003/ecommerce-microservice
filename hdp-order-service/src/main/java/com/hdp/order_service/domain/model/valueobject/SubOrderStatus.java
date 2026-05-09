package com.hdp.order_service.domain.model.valueobject;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public enum SubOrderStatus {
    PAID,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    COMPLETED,
    CANCELLED;

    private static final Map<SubOrderStatus, Set<SubOrderStatus>> VALID_TRANSITIONS = Map.of(
        PAID, EnumSet.of(PROCESSING, CANCELLED),
        PROCESSING, EnumSet.of(SHIPPED, CANCELLED),
        SHIPPED, EnumSet.of(DELIVERED),
        DELIVERED, EnumSet.of(COMPLETED),
        COMPLETED, EnumSet.noneOf(SubOrderStatus.class),
        CANCELLED, EnumSet.noneOf(SubOrderStatus.class)
    );

    public boolean canTransitionTo(SubOrderStatus target) {
        Set<SubOrderStatus> validTargets = VALID_TRANSITIONS.get(this);
        return validTargets != null && validTargets.contains(target);
    }

    public boolean isCancellable() {
        return this == PAID || this == PROCESSING;
    }

    public boolean isTerminal() {
        return this == COMPLETED || this == CANCELLED;
    }
}