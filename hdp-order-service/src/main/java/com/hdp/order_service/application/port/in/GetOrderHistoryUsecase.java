package com.hdp.order_service.application.port.in;

import com.hdp.core.usecase.Usecase;
import com.hdp.order_service.domain.model.valueobject.OrderStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface GetOrderHistoryUsecase extends Usecase<GetOrderHistoryUsecase.Command, GetOrderHistoryUsecase.Result> {

    record Command(UUID orderId) {}

    record Result(List<OrderStatusHistoryResult> histories) {}

    record OrderStatusHistoryResult(
        UUID id, UUID orderId, OrderStatus previousStatus, OrderStatus newStatus,
        UUID changedBy, String reason, Instant createdAt
    ) {}
}
