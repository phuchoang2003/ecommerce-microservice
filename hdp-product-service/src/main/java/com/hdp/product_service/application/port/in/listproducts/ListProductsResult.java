package com.hdp.product_service.application.port.in.listproducts;

import java.util.List;

public record ListProductsResult(
    List<ProductSummary> products,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean hasNext,
    boolean hasPrevious
) {
}
