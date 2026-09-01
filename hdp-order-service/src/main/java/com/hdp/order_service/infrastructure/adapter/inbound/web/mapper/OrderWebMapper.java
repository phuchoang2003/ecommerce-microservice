package com.hdp.order_service.infrastructure.adapter.inbound.web.mapper;

import com.hdp.order_service.application.port.in.cancelorder.CancelOrderCommand;
import com.hdp.order_service.application.port.in.cancelorder.CancelOrderResult;
import com.hdp.order_service.application.port.in.createorder.CreateOrderCommand;
import com.hdp.order_service.application.port.in.createorder.CreateOrderResult;
import com.hdp.order_service.application.port.in.getappliedcoupons.AppliedCouponView;
import com.hdp.order_service.application.port.in.getorder.GetOrderResult;
import com.hdp.order_service.application.port.in.getorderhistory.OrderStatusHistoryView;
import com.hdp.order_service.application.port.in.getorderitems.OrderItemView;
import com.hdp.order_service.application.port.in.listorders.OrderSummary;
import com.hdp.order_service.application.port.in.listsubordersbyorder.SubOrderSummary;
import com.hdp.order_service.application.port.in.updateorderstatus.UpdateOrderStatusCommand;
import com.hdp.order_service.application.port.in.updateorderstatus.UpdateOrderStatusResult;
import com.hdp.order_service.infrastructure.adapter.inbound.web.dto.request.CancelOrderRequest;
import com.hdp.order_service.infrastructure.adapter.inbound.web.dto.request.CreateOrderItemRequest;
import com.hdp.order_service.infrastructure.adapter.inbound.web.dto.request.CreateOrderRequest;
import com.hdp.order_service.infrastructure.adapter.inbound.web.dto.request.UpdateOrderStatusRequest;
import com.hdp.order_service.infrastructure.adapter.inbound.web.dto.response.AppliedCouponResponse;
import com.hdp.order_service.infrastructure.adapter.inbound.web.dto.response.OrderItemResponse;
import com.hdp.order_service.infrastructure.adapter.inbound.web.dto.response.OrderResponse;
import com.hdp.order_service.infrastructure.adapter.inbound.web.dto.response.OrderStatusHistoryResponse;
import com.hdp.order_service.infrastructure.adapter.inbound.web.dto.response.OrderSummaryResponse;
import com.hdp.order_service.infrastructure.adapter.inbound.web.dto.response.SubOrderResponse;
import com.hdp.order_service.infrastructure.adapter.inbound.web.dto.response.SubOrderSummaryResponse;

/**
 * Mapper for OrderController - converts between web DTOs and command/query handlers.
 */
public final class OrderWebMapper {

    private OrderWebMapper() {}

    // ==================== Command Mappers ====================

    public static CreateOrderCommand toCreateOrderCommand(CreateOrderRequest request) {
        return new CreateOrderCommand(
            request.buyerId(),
            request.shippingAddressId(),
            request.paymentMethod(),
            request.items().stream().map(OrderWebMapper::toCreateOrderItemCommand).toList(),
            request.couponCodes()
        );
    }

    private static CreateOrderCommand.CreateOrderItemCommand toCreateOrderItemCommand(CreateOrderItemRequest item) {
        return new CreateOrderCommand.CreateOrderItemCommand(
            item.sellerId(),
            item.productId(),
            item.variantId(),
            item.quantity()
        );
    }

    public static UpdateOrderStatusCommand toUpdateOrderStatusCommand(UUID id, UpdateOrderStatusRequest request) {
        return new UpdateOrderStatusCommand(
            id,
            request.status(),
            request.changedBy(),
            request.reason()
        );
    }

    public static CancelOrderCommand toCancelOrderCommand(UUID id, CancelOrderRequest request) {
        return new CancelOrderCommand(id, request.cancelledBy(), request.reason());
    }

    // ==================== Response Mappers ====================

    public static OrderResponse toResponse(CreateOrderResult r) {
        return OrderResponse.builder()
            .id(r.id()).orderNumber(r.orderNumber()).buyerId(r.buyerId())
            .shippingAddressId(r.shippingAddressId()).paymentMethod(r.paymentMethod()).status(r.status())
            .subtotal(r.subtotal()).shippingFee(r.shippingFee()).discount(r.discount()).tax(r.tax())
            .totalAmount(r.totalAmount()).paymentIntentId(r.paymentIntentId())
            .expiresAt(r.expiresAt()).paidAt(r.paidAt()).cancelledAt(r.cancelledAt())
            .cancellationReason(r.cancellationReason()).createdAt(r.createdAt()).updatedAt(r.updatedAt())
            .subOrders(r.subOrders().stream().map(OrderWebMapper::toSubOrderResponse).toList())
            .items(r.items().stream().map(OrderWebMapper::toItemResponse).toList())
            .appliedCoupons(r.appliedCoupons().stream().map(OrderWebMapper::toCouponResponse).toList())
            .build();
    }

    public static OrderResponse toResponse(GetOrderResult r) {
        return OrderResponse.builder()
            .id(r.id()).orderNumber(r.orderNumber()).buyerId(r.buyerId())
            .shippingAddressId(r.shippingAddressId()).paymentMethod(r.paymentMethod()).status(r.status())
            .subtotal(r.subtotal()).shippingFee(r.shippingFee()).discount(r.discount()).tax(r.tax())
            .totalAmount(r.totalAmount()).paymentIntentId(r.paymentIntentId())
            .expiresAt(r.expiresAt()).paidAt(r.paidAt()).cancelledAt(r.cancelledAt())
            .cancellationReason(r.cancellationReason()).createdAt(r.createdAt()).updatedAt(r.updatedAt())
            .subOrders(r.subOrders().stream().map(OrderWebMapper::toSubOrderResponseGet).toList())
            .items(r.items().stream().map(OrderWebMapper::toItemResponseGet).toList())
            .appliedCoupons(r.appliedCoupons().stream().map(OrderWebMapper::toCouponResponseGet).toList())
            .build();
    }

    public static OrderResponse toResponse(CancelOrderResult r) {
        return OrderResponse.builder()
            .id(r.id()).orderNumber(r.orderNumber()).buyerId(r.buyerId())
            .shippingAddressId(r.shippingAddressId()).paymentMethod(r.paymentMethod()).status(r.status())
            .subtotal(r.subtotal()).shippingFee(r.shippingFee()).discount(r.discount()).tax(r.tax())
            .totalAmount(r.totalAmount()).paymentIntentId(r.paymentIntentId())
            .expiresAt(r.expiresAt()).paidAt(r.paidAt()).cancelledAt(r.cancelledAt())
            .cancellationReason(r.cancellationReason()).createdAt(r.createdAt()).updatedAt(r.updatedAt())
            .subOrders(r.subOrders().stream().map(OrderWebMapper::toSubOrderResponseCancel).toList())
            .items(r.items().stream().map(OrderWebMapper::toItemResponseCancel).toList())
            .appliedCoupons(r.appliedCoupons().stream().map(OrderWebMapper::toCouponResponseCancel).toList())
            .build();
    }

    public static OrderResponse toResponse(UpdateOrderStatusResult r) {
        return OrderResponse.builder()
            .id(r.id()).orderNumber(r.orderNumber()).buyerId(r.buyerId())
            .shippingAddressId(r.shippingAddressId()).paymentMethod(r.paymentMethod()).status(r.status())
            .subtotal(r.subtotal()).shippingFee(r.shippingFee()).discount(r.discount()).tax(r.tax())
            .totalAmount(r.totalAmount()).paymentIntentId(r.paymentIntentId())
            .expiresAt(r.expiresAt()).paidAt(r.paidAt()).cancelledAt(r.cancelledAt())
            .cancellationReason(r.cancellationReason()).createdAt(r.createdAt()).updatedAt(r.updatedAt())
            .subOrders(r.subOrders().stream().map(OrderWebMapper::toSubOrderResponseUpdate).toList())
            .items(r.items().stream().map(OrderWebMapper::toItemResponseUpdate).toList())
            .appliedCoupons(r.appliedCoupons().stream().map(OrderWebMapper::toCouponResponseUpdate).toList())
            .build();
    }

    public static OrderSummaryResponse toResponse(OrderSummary summary) {
        return new OrderSummaryResponse(
            summary.id(), summary.orderNumber(), summary.buyerId(),
            summary.paymentMethod(), summary.status(), summary.totalAmount(), summary.createdAt()
        );
    }

    public static OrderStatusHistoryResponse toResponse(OrderStatusHistoryView r) {
        return new OrderStatusHistoryResponse(
            r.id(), r.orderId(), r.previousStatus(), r.newStatus(),
            r.changedBy(), r.reason(), r.createdAt()
        );
    }

    public static SubOrderSummaryResponse toSubOrderSummaryResponse(SubOrderSummary r) {
        return new SubOrderSummaryResponse(
            r.id(), r.orderId(), r.sellerId(), r.sellerName(), r.status(), r.createdAt()
        );
    }

    public static OrderItemResponse toResponse(OrderItemView r) {
        return OrderItemResponse.builder()
            .id(r.id()).orderId(r.orderId()).subOrderId(r.subOrderId()).sellerId(r.sellerId())
            .productId(r.productId()).variantId(r.variantId()).productName(r.productName())
            .variantName(r.variantName()).price(r.price()).quantity(r.quantity()).subtotal(r.subtotal()).build();
    }

    public static AppliedCouponResponse toResponse(AppliedCouponView r) {
        return AppliedCouponResponse.builder()
            .id(r.id()).orderId(r.orderId()).code(r.code()).couponType(r.couponType()).discountValue(r.discountValue()).build();
    }

    // ==================== Helper Methods ====================

    // CreateOrder helpers
    private static SubOrderResponse toSubOrderResponse(CreateOrderResult.SubOrderResult r) {
        return SubOrderResponse.builder()
            .id(r.id()).orderId(r.orderId()).sellerId(r.sellerId()).sellerName(r.sellerName())
            .status(r.status()).trackingNumber(r.trackingNumber()).carrier(r.carrier())
            .estimatedDelivery(r.estimatedDelivery()).note(r.note())
            .processedAt(r.processedAt()).shippedAt(r.shippedAt()).itemCount(r.itemCount())
            .createdAt(r.createdAt()).updatedAt(r.updatedAt()).build();
    }

    private static OrderItemResponse toItemResponse(CreateOrderResult.OrderItemResult r) {
        return OrderItemResponse.builder()
            .id(r.id()).orderId(r.orderId()).subOrderId(r.subOrderId()).sellerId(r.sellerId())
            .productId(r.productId()).variantId(r.variantId()).productName(r.productName())
            .variantName(r.variantName()).price(r.price()).quantity(r.quantity()).subtotal(r.subtotal()).build();
    }

    private static AppliedCouponResponse toCouponResponse(CreateOrderResult.AppliedCouponResult r) {
        return AppliedCouponResponse.builder()
            .id(r.id()).orderId(r.orderId()).code(r.code()).couponType(r.couponType()).discountValue(r.discountValue()).build();
    }

    // GetOrder helpers
    private static SubOrderResponse toSubOrderResponseGet(GetOrderResult.SubOrderResult r) {
        return SubOrderResponse.builder()
            .id(r.id()).orderId(r.orderId()).sellerId(r.sellerId()).sellerName(r.sellerName())
            .status(r.status()).trackingNumber(r.trackingNumber()).carrier(r.carrier())
            .estimatedDelivery(r.estimatedDelivery()).note(r.note())
            .processedAt(r.processedAt()).shippedAt(r.shippedAt()).itemCount(r.itemCount())
            .createdAt(r.createdAt()).updatedAt(r.updatedAt()).build();
    }

    private static OrderItemResponse toItemResponseGet(GetOrderResult.OrderItemResult r) {
        return OrderItemResponse.builder()
            .id(r.id()).orderId(r.orderId()).subOrderId(r.subOrderId()).sellerId(r.sellerId())
            .productId(r.productId()).variantId(r.variantId()).productName(r.productName())
            .variantName(r.variantName()).price(r.price()).quantity(r.quantity()).subtotal(r.subtotal()).build();
    }

    private static AppliedCouponResponse toCouponResponseGet(GetOrderResult.AppliedCouponResult r) {
        return AppliedCouponResponse.builder()
            .id(r.id()).orderId(r.orderId()).code(r.code()).couponType(r.couponType()).discountValue(r.discountValue()).build();
    }

    // UpdateOrderStatus helpers
    private static SubOrderResponse toSubOrderResponseUpdate(UpdateOrderStatusResult.SubOrderResult r) {
        return SubOrderResponse.builder()
            .id(r.id()).orderId(r.orderId()).sellerId(r.sellerId()).sellerName(r.sellerName())
            .status(r.status()).trackingNumber(r.trackingNumber()).carrier(r.carrier())
            .estimatedDelivery(r.estimatedDelivery()).note(r.note())
            .processedAt(r.processedAt()).shippedAt(r.shippedAt()).itemCount(r.itemCount())
            .createdAt(r.createdAt()).updatedAt(r.updatedAt()).build();
    }

    private static OrderItemResponse toItemResponseUpdate(UpdateOrderStatusResult.OrderItemResult r) {
        return OrderItemResponse.builder()
            .id(r.id()).orderId(r.orderId()).subOrderId(r.subOrderId()).sellerId(r.sellerId())
            .productId(r.productId()).variantId(r.variantId()).productName(r.productName())
            .variantName(r.variantName()).price(r.price()).quantity(r.quantity()).subtotal(r.subtotal()).build();
    }

    private static AppliedCouponResponse toCouponResponseUpdate(UpdateOrderStatusResult.AppliedCouponResult r) {
        return AppliedCouponResponse.builder()
            .id(r.id()).orderId(r.orderId()).code(r.code()).couponType(r.couponType()).discountValue(r.discountValue()).build();
    }

    // CancelOrder helpers
    private static SubOrderResponse toSubOrderResponseCancel(CancelOrderResult.SubOrderResult r) {
        return SubOrderResponse.builder()
            .id(r.id()).orderId(r.orderId()).sellerId(r.sellerId()).sellerName(r.sellerName())
            .status(r.status()).trackingNumber(r.trackingNumber()).carrier(r.carrier())
            .estimatedDelivery(r.estimatedDelivery()).note(r.note())
            .processedAt(r.processedAt()).shippedAt(r.shippedAt()).itemCount(r.itemCount())
            .createdAt(r.createdAt()).updatedAt(r.updatedAt()).build();
    }

    private static OrderItemResponse toItemResponseCancel(CancelOrderResult.OrderItemResult r) {
        return OrderItemResponse.builder()
            .id(r.id()).orderId(r.orderId()).subOrderId(r.subOrderId()).sellerId(r.sellerId())
            .productId(r.productId()).variantId(r.variantId()).productName(r.productName())
            .variantName(r.variantName()).price(r.price()).quantity(r.quantity()).subtotal(r.subtotal()).build();
    }

    private static AppliedCouponResponse toCouponResponseCancel(CancelOrderResult.AppliedCouponResult r) {
        return AppliedCouponResponse.builder()
            .id(r.id()).orderId(r.orderId()).code(r.code()).couponType(r.couponType()).discountValue(r.discountValue()).build();
    }
}
