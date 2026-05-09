package com.hdp.order_service.application.port.in;

import com.hdp.core.usecase.Usecase;
import com.hdp.order_service.domain.model.valueobject.OrderStatus;
import com.hdp.order_service.domain.model.valueobject.PaymentMethod;
import com.hdp.order_service.domain.model.valueobject.SubOrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface UpdateOrderStatusUsecase extends Usecase<UpdateOrderStatusUsecase.Command, UpdateOrderStatusUsecase.Result> {

    record Command(UUID id, OrderStatus status, UUID changedBy, String reason) {}

    record Result(
        UUID id, String orderNumber, UUID buyerId, UUID shippingAddressId,
        PaymentMethod paymentMethod, OrderStatus status,
        BigDecimal subtotal, BigDecimal shippingFee, BigDecimal discount, BigDecimal tax,
        BigDecimal totalAmount, String paymentIntentId,
        Instant expiresAt, Instant paidAt, Instant cancelledAt, String cancellationReason,
        Instant createdAt, Instant updatedAt,
        List<SubOrderResult> subOrders,
        List<OrderItemResult> items,
        List<AppliedCouponResult> appliedCoupons
    ) {}

    record SubOrderResult(
        UUID id, UUID orderId, UUID sellerId, String sellerName,
        SubOrderStatus status, String trackingNumber, String carrier,
        LocalDate estimatedDelivery, String note, Instant processedAt, Instant shippedAt,
        int itemCount, Instant createdAt, Instant updatedAt
    ) {}

    record OrderItemResult(
        UUID id, UUID orderId, UUID subOrderId, UUID sellerId,
        UUID productId, UUID variantId, String productName, String variantName,
        BigDecimal price, Integer quantity, BigDecimal subtotal
    ) {}

    record AppliedCouponResult(
        UUID id, UUID orderId, String code, String couponType, BigDecimal discountValue
    ) {}
}
