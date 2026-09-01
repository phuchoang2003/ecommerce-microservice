package com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.entity;

import com.hdp.common.persistence.entity.BaseEntityJpa;
import com.hdp.order_service.domain.valueobject.OrderStatus;
import com.hdp.order_service.domain.valueobject.PaymentMethod;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "orders")
@AllArgsConstructor
@RequiredArgsConstructor
public class OrderJpa extends BaseEntityJpa {

    @Column(name = "order_number", unique = true, nullable = false)
    private String orderNumber;

    @Column(name = "buyer_id", nullable = false)
    private UUID buyerId;

    @Column(name = "shipping_address_id", nullable = false)
    private UUID shippingAddressId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "subtotal", nullable = false)
    private BigDecimal subtotal;

    @Column(name = "shipping_fee", nullable = false)
    private BigDecimal shippingFee;

    @Column(name = "discount", nullable = false)
    private BigDecimal discount;

    @Column(name = "tax", nullable = false)
    private BigDecimal tax;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "payment_intent_id")
    private String paymentIntentId;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "paid_at")
    private Instant paidAt;

    @Column(name = "cancelled_at")
    private Instant cancelledAt;

    @Column(name = "cancellation_reason")
    private String cancellationReason;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SubOrderJpa> subOrders = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItemJpa> orderItems = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AppliedCouponJpa> appliedCoupons = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderStatusHistoryJpa> statusHistories = new ArrayList<>();
}