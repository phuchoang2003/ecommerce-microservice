package com.hdp.order_service.infrastructure.adapter.inbound.web.dto.response;

import com.hdp.order_service.domain.model.valueobject.OrderStatus;
import com.hdp.order_service.domain.model.valueobject.PaymentMethod;
import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.entity.OrderJpa;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record OrderResponse(
    UUID id,
    String orderNumber,
    UUID buyerId,
    UUID shippingAddressId,
    PaymentMethod paymentMethod,
    OrderStatus status,
    BigDecimal subtotal,
    BigDecimal shippingFee,
    BigDecimal discount,
    BigDecimal tax,
    BigDecimal totalAmount,
    String paymentIntentId,
    Instant expiresAt,
    Instant paidAt,
    Instant cancelledAt,
    String cancellationReason,
    Instant createdAt,
    Instant updatedAt,
    List<SubOrderResponse> subOrders,
    List<OrderItemResponse> items,
    List<AppliedCouponResponse> appliedCoupons
) {
    public static OrderResponse fromEntity(OrderJpa entity) {
        return OrderResponse.builder()
            .id(entity.getId())
            .orderNumber(entity.getOrderNumber())
            .buyerId(entity.getBuyerId())
            .shippingAddressId(entity.getShippingAddressId())
            .paymentMethod(entity.getPaymentMethod())
            .status(entity.getStatus())
            .subtotal(entity.getSubtotal())
            .shippingFee(entity.getShippingFee())
            .discount(entity.getDiscount())
            .tax(entity.getTax())
            .totalAmount(entity.getTotalAmount())
            .paymentIntentId(entity.getPaymentIntentId())
            .expiresAt(entity.getExpiresAt())
            .paidAt(entity.getPaidAt())
            .cancelledAt(entity.getCancelledAt())
            .cancellationReason(entity.getCancellationReason())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .subOrders(entity.getSubOrders() != null
                ? entity.getSubOrders().stream().map(SubOrderResponse::fromEntity).toList()
                : List.of())
            .items(entity.getOrderItems() != null
                ? entity.getOrderItems().stream().map(OrderItemResponse::fromEntity).toList()
                : List.of())
            .appliedCoupons(entity.getAppliedCoupons() != null
                ? entity.getAppliedCoupons().stream().map(AppliedCouponResponse::fromEntity).toList()
                : List.of())
            .build();
    }
}
