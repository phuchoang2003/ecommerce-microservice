package com.hdp.order_service.application.port.in.updatesuborderstatus;

import com.hdp.order_service.domain.valueobject.SubOrderStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record UpdateSubOrderStatusResult(
    UUID id, UUID orderId, UUID sellerId, String sellerName,
    SubOrderStatus status, String trackingNumber, String carrier,
    LocalDate estimatedDelivery, String note, Instant processedAt, Instant shippedAt,
    int itemCount, Instant createdAt, Instant updatedAt
) {
}
