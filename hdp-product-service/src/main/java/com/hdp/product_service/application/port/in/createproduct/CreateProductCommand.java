package com.hdp.product_service.application.port.in.createproduct;

import com.hdp.product_service.domain.model.valueobject.ProductStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateProductCommand(
    UUID sellerId,
    UUID categoryId,
    String name,
    String description,
    BigDecimal price,
    BigDecimal originalPrice,
    List<String> images,
    ProductStatus status
) {
}
