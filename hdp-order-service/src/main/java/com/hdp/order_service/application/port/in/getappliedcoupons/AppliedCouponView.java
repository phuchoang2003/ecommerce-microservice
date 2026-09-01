package com.hdp.order_service.application.port.in.getappliedcoupons;

import java.math.BigDecimal;
import java.util.UUID;

public record AppliedCouponView(
    UUID id, UUID orderId, String code, String couponType, BigDecimal discountValue
) {
}
