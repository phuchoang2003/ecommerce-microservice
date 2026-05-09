package com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.entity;

import com.hdp.common.persistence.entity.BaseEntityJpa;
import com.hdp.order_service.domain.model.valueobject.SubOrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "sub_orders")
public class SubOrderJpa extends BaseEntityJpa {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderJpa order;

    @Column(name = "seller_id", nullable = false)
    private UUID sellerId;

    @Column(name = "seller_name")
    private String sellerName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SubOrderStatus status;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "carrier")
    private String carrier;

    @Column(name = "estimated_delivery")
    private LocalDate estimatedDelivery;

    @Column(name = "note")
    private String note;

    @Column(name = "processed_at")
    private Instant processedAt;

    @Column(name = "shipped_at")
    private Instant shippedAt;

    @OneToMany(mappedBy = "subOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItemJpa> orderItems = new ArrayList<>();
}