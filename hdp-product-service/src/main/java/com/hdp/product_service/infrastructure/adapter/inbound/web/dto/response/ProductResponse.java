package com.hdp.product_service.infrastructure.adapter.inbound.web.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record ProductResponse(
    String id,
    String sellerId,
    String categoryId,
    String name,
    String description,
    BigDecimal price,
    BigDecimal originalPrice,
    List<String> images,
    BigDecimal rating,
    int soldCount,
    String status,
    Instant createdAt,
    Instant updatedAt
) {
}