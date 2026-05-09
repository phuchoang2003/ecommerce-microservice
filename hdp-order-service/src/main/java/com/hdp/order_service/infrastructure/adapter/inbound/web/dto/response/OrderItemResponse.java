package com.hdp.order_service.infrastructure.adapter.inbound.web.dto.response;

import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.entity.OrderItemJpa;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record OrderItemResponse(
    UUID id,
    UUID orderId,
    UUID subOrderId,
    UUID sellerId,
    UUID productId,
    UUID variantId,
    String productName,
    String variantName,
    BigDecimal price,
    Integer quantity,
    BigDecimal subtotal
) {
    public static OrderItemResponse fromEntity(OrderItemJpa entity) {
        return OrderItemResponse.builder()
            .id(entity.getId())
            .orderId(entity.getOrder() != null ? entity.getOrder().getId() : null)
            .subOrderId(entity.getSubOrder() != null ? entity.getSubOrder().getId() : null)
            .sellerId(entity.getSellerId())
            .productId(entity.getProductId())
            .variantId(entity.getVariantId())
            .productName(entity.getProductName())
            .variantName(entity.getVariantName())
            .price(entity.getPrice())
            .quantity(entity.getQuantity())
            .subtotal(entity.getSubtotal())
            .build();
    }
}
