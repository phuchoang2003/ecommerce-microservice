package com.hdp.order_service.infrastructure.adapter.inbound.web.dto.response;

import com.hdp.order_service.domain.model.valueobject.OrderStatus;
import com.hdp.order_service.domain.model.valueobject.PaymentMethod;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderSummaryResponse(
    UUID id, String orderNumber, UUID buyerId,
    PaymentMethod paymentMethod, OrderStatus status,
    BigDecimal totalAmount, Instant createdAt
) {}
