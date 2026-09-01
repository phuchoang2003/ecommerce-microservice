package com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.entity;

import com.hdp.common.persistence.entity.BaseEntityJpa;
import com.hdp.order_service.domain.valueobject.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "order_status_history")
public class OrderStatusHistoryJpa extends BaseEntityJpa {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderJpa order;

    @Enumerated(EnumType.STRING)
    @Column(name = "previous_status")
    private OrderStatus previousStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false)
    private OrderStatus newStatus;

    @Column(name = "changed_by")
    private UUID changedBy;

    @Column(name = "reason")
    private String reason;
}