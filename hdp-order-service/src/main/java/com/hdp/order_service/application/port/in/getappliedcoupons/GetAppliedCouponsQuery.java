package com.hdp.order_service.application.port.in.getappliedcoupons;

import java.util.UUID;

public record GetAppliedCouponsQuery(UUID orderId) {
}
