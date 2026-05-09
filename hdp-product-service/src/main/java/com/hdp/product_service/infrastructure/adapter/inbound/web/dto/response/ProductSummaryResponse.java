package com.hdp.product_service.infrastructure.adapter.inbound.web.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record ProductSummaryResponse(
    String id,
    String sellerId,
    String categoryId,
    String name,
    BigDecimal price,
    List<String> images,
    BigDecimal rating,
    int soldCount,
    String status
) {
}