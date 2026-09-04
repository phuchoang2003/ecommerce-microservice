package com.hdp.order_service.domain.model;

import com.hdp.core.exception.BusinessException;
import com.hdp.order_service.domain.exception.OrderErrorCode;
import com.hdp.order_service.domain.valueobject.SubOrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class SubOrder {
    private final UUID id;
    private final UUID orderId;
    private final UUID sellerId;
    private final String sellerName;
    private SubOrderStatus status;
    private String trackingNumber;
    private String carrier;
    private LocalDate estimatedDelivery;
    private String note;
    private Instant processedAt;
    private Instant shippedAt;
    private final List<OrderItem> items;
    private final Instant createdAt;
    private Instant updatedAt;

    @Builder
    public SubOrder(UUID id, UUID orderId, UUID sellerId, String sellerName,
                    SubOrderStatus status, String trackingNumber, String carrier,
                    LocalDate estimatedDelivery, String note, Instant processedAt,
                    Instant shippedAt, List<OrderItem> items, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.orderId = orderId;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.status = status != null ? status : SubOrderStatus.PAID;
        this.trackingNumber = trackingNumber;
        this.carrier = carrier;
        this.estimatedDelivery = estimatedDelivery;
        this.note = note;
        this.processedAt = processedAt;
        this.shippedAt = shippedAt;
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void updateStatus(SubOrderStatus newStatus) {
        if (!this.status.canTransitionTo(newStatus)) {
            throw new BusinessException(OrderErrorCode.SUBORDER_INVALID_STATUS_TRANSITION,
                this.status, newStatus);
        }
        this.status = newStatus;
        this.updatedAt = Instant.now();

        if (newStatus == SubOrderStatus.PROCESSING) {
            this.processedAt = Instant.now();
        } else if (newStatus == SubOrderStatus.SHIPPED) {
            this.shippedAt = Instant.now();
        }
    }

    public void cancel() {
        if (!this.status.isCancellable()) {
            throw new BusinessException(OrderErrorCode.SUBORDER_NOT_CANCELLABLE,
                this.status);
        }
        updateStatus(SubOrderStatus.CANCELLED);
    }

    public void updateTracking(String trackingNumber, String carrier, LocalDate estimatedDelivery) {
        if (this.status != SubOrderStatus.SHIPPED && this.status != SubOrderStatus.PROCESSING) {
            throw new BusinessException(OrderErrorCode.SUBORDER_INVALID_TRACKING_UPDATE,
                this.status);
        }
        this.trackingNumber = trackingNumber;
        this.carrier = carrier;
        this.estimatedDelivery = estimatedDelivery;
        this.updatedAt = Instant.now();
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
    }

    public void updateNote(String note) {
        this.note = note;
        this.updatedAt = Instant.now();
    }
}