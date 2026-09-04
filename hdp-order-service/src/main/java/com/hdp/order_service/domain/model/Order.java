package com.hdp.order_service.domain.model;

import com.hdp.core.exception.BusinessException;
import com.hdp.order_service.domain.exception.OrderErrorCode;
import com.hdp.order_service.domain.valueobject.OrderId;
import com.hdp.order_service.domain.valueobject.OrderNumber;
import com.hdp.order_service.domain.valueobject.OrderStatus;
import com.hdp.order_service.domain.valueobject.PaymentMethod;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Order {
    private final OrderId id;
    private final OrderNumber orderNumber;
    private final UUID buyerId;
    private final UUID shippingAddressId;
    private final PaymentMethod paymentMethod;
    private OrderStatus status;
    private final BigDecimal subtotal;
    private final BigDecimal shippingFee;
    private final BigDecimal discount;
    private final BigDecimal tax;
    private final BigDecimal totalAmount;
    private String paymentIntentId;
    private final Instant expiresAt;
    private Instant paidAt;
    private Instant cancelledAt;
    private String cancellationReason;
    private final List<SubOrder> subOrders;
    private final List<OrderItem> items;
    private final List<AppliedCoupon> appliedCoupons;
    private final List<OrderStatusHistory> statusHistories;
    private final Instant createdAt;
    private Instant updatedAt;

    @Builder
    public Order(OrderId id, OrderNumber orderNumber, UUID buyerId, UUID shippingAddressId,
                 PaymentMethod paymentMethod, OrderStatus status,
                 BigDecimal subtotal, BigDecimal shippingFee, BigDecimal discount, BigDecimal tax,
                 BigDecimal totalAmount, String paymentIntentId,
                 Instant expiresAt, Instant paidAt, Instant cancelledAt, String cancellationReason,
                 List<SubOrder> subOrders, List<OrderItem> items, List<AppliedCoupon> appliedCoupons,
                 List<OrderStatusHistory> statusHistories, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.buyerId = buyerId;
        this.shippingAddressId = shippingAddressId;
        this.paymentMethod = paymentMethod;
        this.status = status != null ? status : OrderStatus.PENDING;
        this.subtotal = subtotal;
        this.shippingFee = shippingFee;
        this.discount = discount;
        this.tax = tax;
        this.totalAmount = totalAmount;
        this.paymentIntentId = paymentIntentId;
        this.expiresAt = expiresAt;
        this.paidAt = paidAt;
        this.cancelledAt = cancelledAt;
        this.cancellationReason = cancellationReason;
        this.subOrders = subOrders != null ? new ArrayList<>(subOrders) : new ArrayList<>();
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
        this.appliedCoupons = appliedCoupons != null ? new ArrayList<>(appliedCoupons) : new ArrayList<>();
        this.statusHistories = statusHistories != null ? new ArrayList<>(statusHistories) : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void updateStatus(OrderStatus newStatus, UUID changedBy, String reason) {
        if (!this.status.canTransitionTo(newStatus)) {
            throw new BusinessException(OrderErrorCode.ORDER_INVALID_STATUS_TRANSITION,
                this.status, newStatus);
        }

        OrderStatus previousStatus = this.status;
        this.status = newStatus;
        this.updatedAt = Instant.now();

        if (newStatus == OrderStatus.PAID) {
            this.paidAt = Instant.now();
        } else if (newStatus == OrderStatus.CANCELLED) {
            this.cancelledAt = Instant.now();
        }

        addStatusHistory(OrderStatusHistory.create(previousStatus, newStatus, changedBy, reason));
    }

    public void cancel(UUID cancelledBy, String reason) {
        if (!this.status.isCancellable()) {
            throw new BusinessException(OrderErrorCode.ORDER_NOT_CANCELLABLE,
                this.status);
        }
        this.cancellationReason = reason;
        updateStatus(OrderStatus.CANCELLED, cancelledBy, reason);
    }

    private void addStatusHistory(OrderStatusHistory history) {
        this.statusHistories.add(history);
    }

    public void addSubOrder(SubOrder subOrder) {
        this.subOrders.add(subOrder);
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
    }

    public void addCoupon(AppliedCoupon coupon) {
        this.appliedCoupons.add(coupon);
    }

}