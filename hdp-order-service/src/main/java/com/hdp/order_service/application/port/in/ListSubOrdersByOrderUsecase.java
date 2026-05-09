package com.hdp.order_service.application.port.in;

import com.hdp.core.usecase.Usecase;
import com.hdp.order_service.domain.model.valueobject.SubOrderStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ListSubOrdersByOrderUsecase extends Usecase<ListSubOrdersByOrderUsecase.Command, ListSubOrdersByOrderUsecase.Result> {

    record Command(UUID orderId) {}

    record Result(List<SubOrderSummary> subOrderResults) {}

    record SubOrderSummary(
        UUID id, UUID orderId, UUID sellerId, String sellerName,
        SubOrderStatus status, Instant createdAt
    ) {}
}
