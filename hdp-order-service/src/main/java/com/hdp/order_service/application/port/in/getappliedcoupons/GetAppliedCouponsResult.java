package com.hdp.order_service.application.port.in.getappliedcoupons;

import java.util.List;

public record GetAppliedCouponsResult(List<AppliedCouponView> appliedCoupons) {
}
