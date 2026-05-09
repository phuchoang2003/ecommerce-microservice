package com.hdp.order_service.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class AppliedCoupon {
    private final UUID id;
    private final String code;
    private final String couponType;
    private final BigDecimal discountValue;

    @Builder
    public AppliedCoupon(UUID id, String code, String couponType, BigDecimal discountValue) {
        this.id = id;
        this.code = code;
        this.couponType = couponType;
        this.discountValue = discountValue;
    }

    public static AppliedCoupon create(String code, String couponType, BigDecimal discountValue) {
        return AppliedCoupon.builder()
                .code(code)
                .couponType(couponType)
                .discountValue(discountValue)
                .build();
    }
}