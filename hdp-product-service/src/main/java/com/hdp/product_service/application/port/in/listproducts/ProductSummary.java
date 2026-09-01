package com.hdp.product_service.application.port.in.listproducts;

import com.hdp.product_service.domain.model.valueobject.ProductStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ProductSummary(
    UUID id,
    UUID sellerId,
    UUID categoryId,
    String name,
    BigDecimal price,
    List<String> images,
    BigDecimal rating,
    int soldCount,
    ProductStatus status
) {
}
