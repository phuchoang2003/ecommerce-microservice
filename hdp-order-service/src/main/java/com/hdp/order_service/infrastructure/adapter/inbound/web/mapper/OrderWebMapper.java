package com.hdp.order_service.infrastructure.adapter.inbound.web.mapper;

import com.hdp.core.util.EnumUtils;
import com.hdp.order_service.application.port.in.CancelOrderUsecase;
import com.hdp.order_service.application.port.in.CreateOrderUsecase;
import com.hdp.order_service.application.port.in.GetAppliedCouponsUsecase;
import com.hdp.order_service.application.port.in.GetOrderHistoryUsecase;
import com.hdp.order_service.application.port.in.GetOrderItemsUsecase;
import com.hdp.order_service.application.port.in.GetOrderUsecase;
import com.hdp.order_service.application.port.in.ListOrdersUsecase;
import com.hdp.order_service.application.port.in.ListSubOrdersByOrderUsecase;
import com.hdp.order_service.application.port.in.UpdateOrderStatusUsecase;
import com.hdp.order_service.domain.model.valueobject.OrderStatus;
import com.hdp.order_service.domain.model.valueobject.PaymentMethod;
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

import java.util.UUID;

/**
 * Mapper for OrderController - converts between web DTOs and use case commands/results.
 */
public final class OrderWebMapper {

    private OrderWebMapper() {}

    // ==================== Command Mappers ====================

    public static CreateOrderUsecase.Command toCreateOrderCommand(CreateOrderRequest request) {
        return new CreateOrderUsecase.Command(
            request.buyerId(),
            request.shippingAddressId(),
            EnumUtils.fromString(PaymentMethod.class, request.paymentMethod()),
            request.items().stream().map(OrderWebMapper::toCreateOrderItemCommand).toList(),
            request.couponCodes()
        );
    }

    private static CreateOrderUsecase.CreateOrderItemCommand toCreateOrderItemCommand(CreateOrderItemRequest item) {
        return new CreateOrderUsecase.CreateOrderItemCommand(
            item.sellerId(),
            item.productId(),
            item.variantId(),
            item.quantity()
        );
    }


    public static ListOrdersUsecase.Command toListOrdersQuery(UUID buyerId, OrderStatus status, int page, int size) {
        return new ListOrdersUsecase.Command(buyerId, status, page, size);
    }

    public static UpdateOrderStatusUsecase.Command toUpdateOrderStatusCommand(UUID id, UpdateOrderStatusRequest request) {
        return new UpdateOrderStatusUsecase.Command(
            id,
            EnumUtils.fromString(OrderStatus.class, request.status()),
            request.changedBy(),
            request.reason()
        );
    }

    public static CancelOrderUsecase.Command toCancelOrderCommand(UUID id, CancelOrderRequest request) {
        return new CancelOrderUsecase.Command(id, request.cancelledBy(), request.reason());
    }

    public static GetOrderUsecase.Command toGetOrderQuery(UUID id) {
        return new GetOrderUsecase.Command(id);
    }

    public static GetOrderHistoryUsecase.Command toGetOrderHistoryQuery(UUID id) {
        return new GetOrderHistoryUsecase.Command(id);
    }

    public static ListSubOrdersByOrderUsecase.Command toListSubOrdersQuery(UUID orderId) {
        return new ListSubOrdersByOrderUsecase.Command(orderId);
    }

    public static GetOrderItemsUsecase.Query toGetOrderItemsQuery(UUID orderId) {
        return new GetOrderItemsUsecase.Query(orderId);
    }

    public static GetAppliedCouponsUsecase.Query toGetAppliedCouponsQuery(UUID orderId) {
        return new GetAppliedCouponsUsecase.Query(orderId);
    }

    // ==================== Response Mappers ====================

    public static OrderResponse toResponse(CreateOrderUsecase.Result r) {
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


    public static OrderSummaryResponse toResponse(ListOrdersUsecase.OrderSummary summary) {
        return new OrderSummaryResponse(
            summary.id(), summary.orderNumber(), summary.buyerId(),
            summary.paymentMethod(), summary.status(), summary.totalAmount(), summary.createdAt()
        );
    }

    public static OrderStatusHistoryResponse toResponse(GetOrderHistoryUsecase.OrderStatusHistoryResult r) {
        return new OrderStatusHistoryResponse(
            r.id(), r.orderId(), r.previousStatus(), r.newStatus(),
            r.changedBy(), r.reason(), r.createdAt()
        );
    }

    public static SubOrderSummaryResponse toSubOrderSummaryResponse(ListSubOrdersByOrderUsecase.SubOrderSummary r) {
        return new SubOrderSummaryResponse(
            r.id(), r.orderId(), r.sellerId(), r.sellerName(), r.status(), r.createdAt()
        );
    }

    public static OrderItemResponse toResponse(GetOrderItemsUsecase.OrderItemResult r) {
        return OrderItemResponse.builder()
            .id(r.id()).orderId(r.orderId()).subOrderId(r.subOrderId()).sellerId(r.sellerId())
            .productId(r.productId()).variantId(r.variantId()).productName(r.productName())
            .variantName(r.variantName()).price(r.price()).quantity(r.quantity()).subtotal(r.subtotal()).build();
    }

    public static AppliedCouponResponse toResponse(GetAppliedCouponsUsecase.AppliedCouponResult r) {
        return AppliedCouponResponse.builder()
            .id(r.id()).orderId(r.orderId()).code(r.code()).couponType(r.couponType()).discountValue(r.discountValue()).build();
    }

    // ==================== Response Mappers (Get, Cancel) ====================

    public static OrderResponse toResponse(GetOrderUsecase.Result r) {
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

    public static OrderResponse toResponse(CancelOrderUsecase.Result r) {
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

    public static OrderResponse toResponse(UpdateOrderStatusUsecase.Result r) {
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

    // ==================== Helper Methods ====================

    // CreateOrder helpers
    private static SubOrderResponse toSubOrderResponse(CreateOrderUsecase.SubOrderResult r) {
        return SubOrderResponse.builder()
            .id(r.id()).orderId(r.orderId()).sellerId(r.sellerId()).sellerName(r.sellerName())
            .status(r.status()).trackingNumber(r.trackingNumber()).carrier(r.carrier())
            .estimatedDelivery(r.estimatedDelivery()).note(r.note())
            .processedAt(r.processedAt()).shippedAt(r.shippedAt()).itemCount(r.itemCount())
            .createdAt(r.createdAt()).updatedAt(r.updatedAt()).build();
    }

    private static OrderItemResponse toItemResponse(CreateOrderUsecase.OrderItemResult r) {
        return OrderItemResponse.builder()
            .id(r.id()).orderId(r.orderId()).subOrderId(r.subOrderId()).sellerId(r.sellerId())
            .productId(r.productId()).variantId(r.variantId()).productName(r.productName())
            .variantName(r.variantName()).price(r.price()).quantity(r.quantity()).subtotal(r.subtotal()).build();
    }

    private static AppliedCouponResponse toCouponResponse(CreateOrderUsecase.AppliedCouponResult r) {
        return AppliedCouponResponse.builder()
            .id(r.id()).orderId(r.orderId()).code(r.code()).couponType(r.couponType()).discountValue(r.discountValue()).build();
    }

    // GetOrder helpers
    private static SubOrderResponse toSubOrderResponseGet(GetOrderUsecase.SubOrderResult r) {
        return SubOrderResponse.builder()
            .id(r.id()).orderId(r.orderId()).sellerId(r.sellerId()).sellerName(r.sellerName())
            .status(r.status()).trackingNumber(r.trackingNumber()).carrier(r.carrier())
            .estimatedDelivery(r.estimatedDelivery()).note(r.note())
            .processedAt(r.processedAt()).shippedAt(r.shippedAt()).itemCount(r.itemCount())
            .createdAt(r.createdAt()).updatedAt(r.updatedAt()).build();
    }

    private static OrderItemResponse toItemResponseGet(GetOrderUsecase.OrderItemResult r) {
        return OrderItemResponse.builder()
            .id(r.id()).orderId(r.orderId()).subOrderId(r.subOrderId()).sellerId(r.sellerId())
            .productId(r.productId()).variantId(r.variantId()).productName(r.productName())
            .variantName(r.variantName()).price(r.price()).quantity(r.quantity()).subtotal(r.subtotal()).build();
    }

    private static AppliedCouponResponse toCouponResponseGet(GetOrderUsecase.AppliedCouponResult r) {
        return AppliedCouponResponse.builder()
            .id(r.id()).orderId(r.orderId()).code(r.code()).couponType(r.couponType()).discountValue(r.discountValue()).build();
    }

    // UpdateOrderStatus helpers
    private static SubOrderResponse toSubOrderResponseUpdate(UpdateOrderStatusUsecase.SubOrderResult r) {
        return SubOrderResponse.builder()
            .id(r.id()).orderId(r.orderId()).sellerId(r.sellerId()).sellerName(r.sellerName())
            .status(r.status()).trackingNumber(r.trackingNumber()).carrier(r.carrier())
            .estimatedDelivery(r.estimatedDelivery()).note(r.note())
            .processedAt(r.processedAt()).shippedAt(r.shippedAt()).itemCount(r.itemCount())
            .createdAt(r.createdAt()).updatedAt(r.updatedAt()).build();
    }

    private static OrderItemResponse toItemResponseUpdate(UpdateOrderStatusUsecase.OrderItemResult r) {
        return OrderItemResponse.builder()
            .id(r.id()).orderId(r.orderId()).subOrderId(r.subOrderId()).sellerId(r.sellerId())
            .productId(r.productId()).variantId(r.variantId()).productName(r.productName())
            .variantName(r.variantName()).price(r.price()).quantity(r.quantity()).subtotal(r.subtotal()).build();
    }

    private static AppliedCouponResponse toCouponResponseUpdate(UpdateOrderStatusUsecase.AppliedCouponResult r) {
        return AppliedCouponResponse.builder()
            .id(r.id()).orderId(r.orderId()).code(r.code()).couponType(r.couponType()).discountValue(r.discountValue()).build();
    }

    // CancelOrder helpers
    private static SubOrderResponse toSubOrderResponseCancel(CancelOrderUsecase.SubOrderResult r) {
        return SubOrderResponse.builder()
            .id(r.id()).orderId(r.orderId()).sellerId(r.sellerId()).sellerName(r.sellerName())
            .status(r.status()).trackingNumber(r.trackingNumber()).carrier(r.carrier())
            .estimatedDelivery(r.estimatedDelivery()).note(r.note())
            .processedAt(r.processedAt()).shippedAt(r.shippedAt()).itemCount(r.itemCount())
            .createdAt(r.createdAt()).updatedAt(r.updatedAt()).build();
    }

    private static OrderItemResponse toItemResponseCancel(CancelOrderUsecase.OrderItemResult r) {
        return OrderItemResponse.builder()
            .id(r.id()).orderId(r.orderId()).subOrderId(r.subOrderId()).sellerId(r.sellerId())
            .productId(r.productId()).variantId(r.variantId()).productName(r.productName())
            .variantName(r.variantName()).price(r.price()).quantity(r.quantity()).subtotal(r.subtotal()).build();
    }

    private static AppliedCouponResponse toCouponResponseCancel(CancelOrderUsecase.AppliedCouponResult r) {
        return AppliedCouponResponse.builder()
            .id(r.id()).orderId(r.orderId()).code(r.code()).couponType(r.couponType()).discountValue(r.discountValue()).build();
    }
}
