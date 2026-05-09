package com.hdp.order_service.infrastructure.adapter.inbound.web.dto.response;

import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.entity.AppliedCouponJpa;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record AppliedCouponResponse(
    UUID id,
    UUID orderId,
    String code,
    String couponType,
    BigDecimal discountValue
) {
    public static AppliedCouponResponse fromEntity(AppliedCouponJpa entity) {
        return AppliedCouponResponse.builder()
            .id(entity.getId())
            .orderId(entity.getOrder() != null ? entity.getOrder().getId() : null)
            .code(entity.getCode())
            .couponType(entity.getCouponType())
            .discountValue(entity.getDiscountValue())
            .build();
    }
}
