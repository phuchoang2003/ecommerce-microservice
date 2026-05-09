package com.hdp.product_service.application.port.in;

import com.hdp.core.usecase.Usecase;
import com.hdp.core.request.PageQuery;
import com.hdp.product_service.domain.model.valueobject.ProductStatus;

import java.util.UUID;

public interface ListProductsUsecase extends Usecase<ListProductsUsecase.Command, ListProductsUsecase.Result> {

    record Command(
        PageQuery pageQuery,
        UUID sellerId,
        UUID categoryId,
        ProductStatus status,
        String name
    ) {}

    record Result(
        java.util.List<CreateProductUsecase.Result> products,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious
    ) {}
}