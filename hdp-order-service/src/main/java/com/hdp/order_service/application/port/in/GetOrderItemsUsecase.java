package com.hdp.order_service.application.port.in;

import com.hdp.core.usecase.Usecase;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface GetOrderItemsUsecase extends Usecase<GetOrderItemsUsecase.Query, GetOrderItemsUsecase.Result> {

    record Query(UUID orderId) {}

    record Result(List<OrderItemResult> items) {}

    record OrderItemResult(
        UUID id, UUID orderId, UUID subOrderId, UUID sellerId,
        UUID productId, UUID variantId, String productName, String variantName,
        BigDecimal price, Integer quantity, BigDecimal subtotal
    ) {}
}
