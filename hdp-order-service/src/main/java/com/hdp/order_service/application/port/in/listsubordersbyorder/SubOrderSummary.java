package com.hdp.order_service.application.port.in.listsubordersbyorder;

import com.hdp.order_service.domain.valueobject.SubOrderStatus;

import java.time.Instant;
import java.util.UUID;

public record SubOrderSummary(
    UUID id, UUID orderId, UUID sellerId, String sellerName,
    SubOrderStatus status, Instant createdAt
) {
}
