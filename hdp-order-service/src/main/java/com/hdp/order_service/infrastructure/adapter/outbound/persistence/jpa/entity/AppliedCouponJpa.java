package com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.entity;

import com.hdp.common.persistence.entity.BaseEntityJpa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "applied_coupons")
public class AppliedCouponJpa extends BaseEntityJpa {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderJpa order;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "coupon_type", nullable = false)
    private String couponType;

    @Column(name = "discount_value", nullable = false)
    private BigDecimal discountValue;
}