package com.hdp.product_service.application.port.in.getproduct;

import com.hdp.product_service.domain.model.valueobject.ProductStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record GetProductResult(
    UUID id,
    UUID sellerId,
    UUID categoryId,
    String name,
    String description,
    BigDecimal price,
    BigDecimal originalPrice,
    List<String> images,
    BigDecimal rating,
    int soldCount,
    ProductStatus status,
    Instant createdAt,
    Instant updatedAt
) {
}
