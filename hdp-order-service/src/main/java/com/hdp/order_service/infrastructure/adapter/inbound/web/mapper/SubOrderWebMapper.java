package com.hdp.order_service.infrastructure.adapter.inbound.web.mapper;

import com.hdp.core.util.EnumUtils;
import com.hdp.order_service.application.port.in.GetSubOrderItemsUsecase;
import com.hdp.order_service.application.port.in.GetSubOrderUsecase;
import com.hdp.order_service.application.port.in.UpdateSubOrderStatusUsecase;
import com.hdp.order_service.application.port.in.UpdateSubOrderTrackingUsecase;
import com.hdp.order_service.domain.model.valueobject.SubOrderStatus;
import com.hdp.order_service.infrastructure.adapter.inbound.web.dto.request.UpdateSubOrderStatusRequest;
import com.hdp.order_service.infrastructure.adapter.inbound.web.dto.request.UpdateSubOrderTrackingRequest;
import com.hdp.order_service.infrastructure.adapter.inbound.web.dto.response.OrderItemResponse;
import com.hdp.order_service.infrastructure.adapter.inbound.web.dto.response.SubOrderResponse;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Mapper for SubOrderController - converts between web DTOs and use case commands/results.
 */
public final class SubOrderWebMapper {

    private SubOrderWebMapper() {}

    // ==================== Command Mappers ====================

    public static GetSubOrderUsecase.Command toGetSubOrderQuery(UUID id) {
        return new GetSubOrderUsecase.Command(id);
    }

    public static UpdateSubOrderStatusUsecase.Command toUpdateSubOrderStatusCommand(UUID id, UpdateSubOrderStatusRequest request) {
        return new UpdateSubOrderStatusUsecase.Command(
            id,
            EnumUtils.fromString(SubOrderStatus.class, request.status()),
            request.changedBy(),
            request.reason()
        );
    }

    public static UpdateSubOrderTrackingUsecase.Command toUpdateSubOrderTrackingCommand(UUID id, UpdateSubOrderTrackingRequest request) {
        return new UpdateSubOrderTrackingUsecase.Command(
            id,
            request.trackingNumber(),
            request.carrier(),
            request.estimatedDelivery(),
            request.note()
        );
    }

    public static GetSubOrderItemsUsecase.Command toGetSubOrderItemsQuery(UUID subOrderId) {
        return new GetSubOrderItemsUsecase.Command(subOrderId);
    }

    // ==================== Response Mappers ====================

    public static SubOrderResponse toResponse(GetSubOrderUsecase.Result r) {
        return toSubOrderResponse(
            r.id(), r.orderId(), r.sellerId(), r.sellerName(), r.status(),
            r.trackingNumber(), r.carrier(), r.estimatedDelivery(), r.note(),
            r.processedAt(), r.shippedAt(), r.itemCount(), r.createdAt(), r.updatedAt()
        );
    }

    public static SubOrderResponse toResponse(UpdateSubOrderStatusUsecase.Result r) {
        return toSubOrderResponse(
            r.id(), r.orderId(), r.sellerId(), r.sellerName(), r.status(),
            r.trackingNumber(), r.carrier(), r.estimatedDelivery(), r.note(),
            r.processedAt(), r.shippedAt(), r.itemCount(), r.createdAt(), r.updatedAt()
        );
    }

    public static SubOrderResponse toResponse(UpdateSubOrderTrackingUsecase.Result r) {
        return toSubOrderResponse(
            r.id(), r.orderId(), r.sellerId(), r.sellerName(), r.status(),
            r.trackingNumber(), r.carrier(), r.estimatedDelivery(), r.note(),
            r.processedAt(), r.shippedAt(), r.itemCount(), r.createdAt(), r.updatedAt()
        );
    }

    public static OrderItemResponse toResponse(GetSubOrderItemsUsecase.OrderItemResult r) {
        return OrderItemResponse.builder()
            .id(r.id()).orderId(r.orderId()).subOrderId(r.subOrderId()).sellerId(r.sellerId())
            .productId(r.productId()).variantId(r.variantId()).productName(r.productName())
            .variantName(r.variantName()).price(r.price()).quantity(r.quantity()).subtotal(r.subtotal()).build();
    }

    // ==================== Helper Methods ====================

    private static SubOrderResponse toSubOrderResponse(
            UUID id, UUID orderId, UUID sellerId, String sellerName,
            SubOrderStatus status,
            String trackingNumber, String carrier, LocalDate estimatedDelivery, String note,
            Instant processedAt, Instant shippedAt,
            int itemCount, Instant createdAt, Instant updatedAt) {
        return SubOrderResponse.builder()
            .id(id).orderId(orderId).sellerId(sellerId).sellerName(sellerName)
            .status(status).trackingNumber(trackingNumber).carrier(carrier)
            .estimatedDelivery(estimatedDelivery).note(note)
            .processedAt(processedAt).shippedAt(shippedAt).itemCount(itemCount)
            .createdAt(createdAt).updatedAt(updatedAt).build();
    }
}
