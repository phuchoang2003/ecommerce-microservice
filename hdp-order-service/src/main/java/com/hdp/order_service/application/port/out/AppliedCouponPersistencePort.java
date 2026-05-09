package com.hdp.order_service.application.port.out;

import com.hdp.order_service.domain.model.AppliedCoupon;

import java.util.List;
import java.util.UUID;

public interface AppliedCouponPersistencePort {
    List<AppliedCoupon> findByOrderId(UUID orderId);
}