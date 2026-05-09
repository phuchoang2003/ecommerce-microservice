package com.hdp.order_service.infrastructure.adapter.outbound.persistence.mapper;

import com.hdp.order_service.domain.model.*;
import com.hdp.order_service.domain.model.valueobject.OrderStatus;
import com.hdp.order_service.domain.model.valueobject.PaymentMethod;
import com.hdp.order_service.domain.model.valueobject.SubOrderStatus;
import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public Order toDomain(OrderJpa jpa) {
        if (jpa == null) return null;

        return Order.builder()
                .id(jpa.getId())
                .orderNumber(jpa.getOrderNumber())
                .buyerId(jpa.getBuyerId())
                .shippingAddressId(jpa.getShippingAddressId())
                .paymentMethod(jpa.getPaymentMethod())
                .status(jpa.getStatus())
                .subtotal(jpa.getSubtotal())
                .shippingFee(jpa.getShippingFee())
                .discount(jpa.getDiscount())
                .tax(jpa.getTax())
                .totalAmount(jpa.getTotalAmount())
                .paymentIntentId(jpa.getPaymentIntentId())
                .expiresAt(jpa.getExpiresAt())
                .paidAt(jpa.getPaidAt())
                .cancelledAt(jpa.getCancelledAt())
                .cancellationReason(jpa.getCancellationReason())
                .subOrders(mapSubOrders(jpa))
                .items(mapOrderItems(jpa))
                .appliedCoupons(mapAppliedCoupons(jpa))
                .statusHistories(mapStatusHistories(jpa))
                .createdAt(jpa.getCreatedAt())
                .updatedAt(jpa.getUpdatedAt())
                .build();
    }

    public OrderJpa toJpa(Order domain) {
        if (domain == null) return null;

        OrderJpa jpa = OrderJpa.builder()
                .orderNumber(domain.getOrderNumber())
                .buyerId(domain.getBuyerId())
                .shippingAddressId(domain.getShippingAddressId())
                .paymentMethod(domain.getPaymentMethod())
                .status(domain.getStatus())
                .subtotal(domain.getSubtotal())
                .shippingFee(domain.getShippingFee())
                .discount(domain.getDiscount())
                .tax(domain.getTax())
                .totalAmount(domain.getTotalAmount())
                .paymentIntentId(domain.getPaymentIntentId())
                .expiresAt(domain.getExpiresAt())
                .paidAt(domain.getPaidAt())
                .cancelledAt(domain.getCancelledAt())
                .cancellationReason(domain.getCancellationReason())
                .build();

        if (domain.getId() != null) {
            jpa.setId(domain.getId());
        }
        if (domain.getCreatedAt() != null) {
            jpa.setCreatedAt(domain.getCreatedAt());
        }
        if (domain.getUpdatedAt() != null) {
            jpa.setUpdatedAt(domain.getUpdatedAt());
        }

        return jpa;
    }

    private List<SubOrder> mapSubOrders(OrderJpa jpa) {
        if (jpa.getSubOrders() == null) return List.of();
        return jpa.getSubOrders().stream()
                .map(this::toSubOrderDomain)
                .collect(Collectors.toList());
    }

    private List<OrderItem> mapOrderItems(OrderJpa jpa) {
        if (jpa.getOrderItems() == null) return List.of();
        return jpa.getOrderItems().stream()
                .map(this::toOrderItemDomain)
                .collect(Collectors.toList());
    }

    private List<AppliedCoupon> mapAppliedCoupons(OrderJpa jpa) {
        if (jpa.getAppliedCoupons() == null) return List.of();
        return jpa.getAppliedCoupons().stream()
                .map(this::toAppliedCouponDomain)
                .collect(Collectors.toList());
    }

    private List<OrderStatusHistory> mapStatusHistories(OrderJpa jpa) {
        if (jpa.getStatusHistories() == null) return List.of();
        return jpa.getStatusHistories().stream()
                .map(this::toStatusHistoryDomain)
                .collect(Collectors.toList());
    }

    public SubOrder toSubOrderDomain(SubOrderJpa jpa) {
        if (jpa == null) return null;

        return SubOrder.builder()
                .id(jpa.getId())
                .orderId(jpa.getOrder() != null ? jpa.getOrder().getId() : null)
                .sellerId(jpa.getSellerId())
                .sellerName(jpa.getSellerName())
                .status(jpa.getStatus())
                .trackingNumber(jpa.getTrackingNumber())
                .carrier(jpa.getCarrier())
                .estimatedDelivery(jpa.getEstimatedDelivery())
                .note(jpa.getNote())
                .processedAt(jpa.getProcessedAt())
                .shippedAt(jpa.getShippedAt())
                .items(mapSubOrderItems(jpa))
                .createdAt(jpa.getCreatedAt())
                .updatedAt(jpa.getUpdatedAt())
                .build();
    }

    public SubOrderJpa toSubOrderJpa(SubOrder domain) {
        if (domain == null) return null;

        return SubOrderJpa.builder()
                .sellerId(domain.getSellerId())
                .sellerName(domain.getSellerName())
                .status(domain.getStatus())
                .trackingNumber(domain.getTrackingNumber())
                .carrier(domain.getCarrier())
                .estimatedDelivery(domain.getEstimatedDelivery())
                .note(domain.getNote())
                .processedAt(domain.getProcessedAt())
                .shippedAt(domain.getShippedAt())
                .build();
    }

    private List<OrderItem> mapSubOrderItems(SubOrderJpa jpa) {
        if (jpa.getOrderItems() == null) return List.of();
        return jpa.getOrderItems().stream()
                .map(this::toOrderItemDomain)
                .collect(Collectors.toList());
    }

    public OrderItem toOrderItemDomain(OrderItemJpa jpa) {
        if (jpa == null) return null;

        return OrderItem.builder()
                .id(jpa.getId())
                .sellerId(jpa.getSellerId())
                .productId(jpa.getProductId())
                .variantId(jpa.getVariantId())
                .productName(jpa.getProductName())
                .variantName(jpa.getVariantName())
                .price(jpa.getPrice())
                .quantity(jpa.getQuantity())
                .subtotal(jpa.getSubtotal())
                .build();
    }

    public OrderItemJpa toOrderItemJpa(OrderItem domain) {
        if (domain == null) return null;

        return OrderItemJpa.builder()
                .sellerId(domain.getSellerId())
                .productId(domain.getProductId())
                .variantId(domain.getVariantId())
                .productName(domain.getProductName())
                .variantName(domain.getVariantName())
                .price(domain.getPrice())
                .quantity(domain.getQuantity())
                .subtotal(domain.getSubtotal())
                .build();
    }

    public AppliedCoupon toAppliedCouponDomain(AppliedCouponJpa jpa) {
        if (jpa == null) return null;

        return AppliedCoupon.builder()
                .id(jpa.getId())
                .code(jpa.getCode())
                .couponType(jpa.getCouponType())
                .discountValue(jpa.getDiscountValue())
                .build();
    }

    public AppliedCouponJpa toAppliedCouponJpa(AppliedCoupon domain) {
        if (domain == null) return null;

        return AppliedCouponJpa.builder()
                .code(domain.getCode())
                .couponType(domain.getCouponType())
                .discountValue(domain.getDiscountValue())
                .build();
    }

    public OrderStatusHistory toStatusHistoryDomain(OrderStatusHistoryJpa jpa) {
        if (jpa == null) return null;

        return OrderStatusHistory.builder()
                .id(jpa.getId())
                .previousStatus(jpa.getPreviousStatus())
                .newStatus(jpa.getNewStatus())
                .changedBy(jpa.getChangedBy())
                .reason(jpa.getReason())
                .createdAt(jpa.getCreatedAt())
                .build();
    }

    public OrderStatusHistoryJpa toStatusHistoryJpa(OrderStatusHistory domain) {
        if (domain == null) return null;

        return OrderStatusHistoryJpa.builder()
                .previousStatus(domain.getPreviousStatus())
                .newStatus(domain.getNewStatus())
                .changedBy(domain.getChangedBy())
                .reason(domain.getReason())
                .build();
    }
}