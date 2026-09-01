package com.hdp.order_service.infrastructure.adapter.inbound.web.dto.response;

import com.hdp.order_service.domain.valueobject.SubOrderStatus;
import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.entity.SubOrderJpa;
import lombok.Builder;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Builder
public record SubOrderResponse(
    UUID id,
    UUID orderId,
    UUID sellerId,
    String sellerName,
    SubOrderStatus status,
    String trackingNumber,
    String carrier,
    LocalDate estimatedDelivery,
    String note,
    Instant processedAt,
    Instant shippedAt,
    int itemCount,
    Instant createdAt,
    Instant updatedAt
) {
    public static SubOrderResponse fromEntity(SubOrderJpa entity) {
        return SubOrderResponse.builder()
            .id(entity.getId())
            .orderId(entity.getOrder() != null ? entity.getOrder().getId() : null)
            .sellerId(entity.getSellerId())
            .sellerName(entity.getSellerName())
            .status(entity.getStatus())
            .trackingNumber(entity.getTrackingNumber())
            .carrier(entity.getCarrier())
            .estimatedDelivery(entity.getEstimatedDelivery())
            .note(entity.getNote())
            .processedAt(entity.getProcessedAt())
            .shippedAt(entity.getShippedAt())
            .itemCount(entity.getOrderItems() != null ? entity.getOrderItems().size() : 0)
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}
