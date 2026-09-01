package com.hdp.product_service.application.port.in.listproducts;

import com.hdp.product_service.domain.model.valueobject.ProductStatus;

import java.util.UUID;

public record ListProductsQuery(
    int page,
    int size,
    UUID sellerId,
    UUID categoryId,
    ProductStatus status,
    String name
) {
}
