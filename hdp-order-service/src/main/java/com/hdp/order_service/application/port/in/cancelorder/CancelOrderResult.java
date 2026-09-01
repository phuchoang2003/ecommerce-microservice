package com.hdp.order_service.application.port.in.cancelorder;

import com.hdp.order_service.domain.valueobject.OrderStatus;
import com.hdp.order_service.domain.valueobject.PaymentMethod;
import com.hdp.order_service.domain.valueobject.SubOrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CancelOrderResult(
    UUID id, String orderNumber, UUID buyerId, UUID shippingAddressId,
    PaymentMethod paymentMethod, OrderStatus status,
    BigDecimal subtotal, BigDecimal shippingFee, BigDecimal discount, BigDecimal tax,
    BigDecimal totalAmount, String paymentIntentId,
    Instant expiresAt, Instant paidAt, Instant cancelledAt, String cancellationReason,
    Instant createdAt, Instant updatedAt,
    List<CancelOrderResult.SubOrderResult> subOrders,
    List<CancelOrderResult.OrderItemResult> items,
    List<CancelOrderResult.AppliedCouponResult> appliedCoupons
) {
    public record SubOrderResult(
        UUID id, UUID orderId, UUID sellerId, String sellerName,
        SubOrderStatus status, String trackingNumber, String carrier,
        LocalDate estimatedDelivery, String note, Instant processedAt, Instant shippedAt,
        int itemCount, Instant createdAt, Instant updatedAt
    ) {
    }

    public record OrderItemResult(
        UUID id, UUID orderId, UUID subOrderId, UUID sellerId,
        UUID productId, UUID variantId, String productName, String variantName,
        BigDecimal price, Integer quantity, BigDecimal subtotal
    ) {
    }

    public record AppliedCouponResult(
        UUID id, UUID orderId, String code, String couponType, BigDecimal discountValue
    ) {
    }
}
