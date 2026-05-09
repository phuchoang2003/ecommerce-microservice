package com.hdp.order_service.application.port.in;

import com.hdp.core.usecase.Usecase;
import com.hdp.order_service.domain.model.valueobject.SubOrderStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public interface UpdateSubOrderTrackingUsecase extends Usecase<UpdateSubOrderTrackingUsecase.Command, UpdateSubOrderTrackingUsecase.Result> {

    record Command(UUID id, String trackingNumber, String carrier, LocalDate estimatedDelivery, String note) {}

    record Result(
        UUID id, UUID orderId, UUID sellerId, String sellerName,
        SubOrderStatus status, String trackingNumber, String carrier,
        LocalDate estimatedDelivery, String note, Instant processedAt, Instant shippedAt,
        int itemCount, Instant createdAt, Instant updatedAt
    ) {}
}
