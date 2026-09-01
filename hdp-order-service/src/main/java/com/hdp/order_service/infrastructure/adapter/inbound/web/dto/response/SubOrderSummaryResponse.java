package com.hdp.order_service.infrastructure.adapter.inbound.web.dto.response;

import com.hdp.order_service.domain.valueobject.SubOrderStatus;

import java.time.Instant;
import java.util.UUID;

public record SubOrderSummaryResponse(
    UUID id, UUID orderId, UUID sellerId, String sellerName,
    SubOrderStatus status, Instant createdAt
) {}
