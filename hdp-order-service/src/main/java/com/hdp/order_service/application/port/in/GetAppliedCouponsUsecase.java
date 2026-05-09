package com.hdp.order_service.application.port.in;

import com.hdp.core.usecase.Usecase;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface GetAppliedCouponsUsecase extends Usecase<GetAppliedCouponsUsecase.Query, GetAppliedCouponsUsecase.Result> {

    record Query(UUID orderId) {}

    record Result(List<AppliedCouponResult> appliedCoupons) {}

    record AppliedCouponResult(
        UUID id, UUID orderId, String code, String couponType, BigDecimal discountValue
    ) {}
}
