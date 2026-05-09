package com.hdp.order_service.application.port.in;

import com.hdp.core.usecase.Usecase;
import com.hdp.order_service.domain.model.valueobject.OrderStatus;
import com.hdp.order_service.domain.model.valueobject.PaymentMethod;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ListOrdersUsecase extends Usecase<ListOrdersUsecase.Command, ListOrdersUsecase.Result> {

    record Command(UUID buyerId, OrderStatus status, int page, int size) {}

    record Result(List<OrderSummary> orderResults) {}

    record OrderSummary(
        UUID id, String orderNumber, UUID buyerId,
        PaymentMethod paymentMethod, OrderStatus status,
        BigDecimal totalAmount, Instant createdAt
    ) {}
}
