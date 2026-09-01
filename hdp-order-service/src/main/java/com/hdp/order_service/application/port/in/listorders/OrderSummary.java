package com.hdp.order_service.application.port.in.listorders;

import com.hdp.order_service.domain.model.valueobject.OrderStatus;
import com.hdp.order_service.domain.model.valueobject.PaymentMethod;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderSummary(
    UUID id, String orderNumber, UUID buyerId,
    PaymentMethod paymentMethod, OrderStatus status,
    BigDecimal totalAmount, Instant createdAt
) {
}
