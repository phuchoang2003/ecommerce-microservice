package com.hdp.product_service.application.port.in;

import com.hdp.core.usecase.Usecase;
import com.hdp.product_service.domain.model.valueobject.ProductStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface UpdateProductUsecase extends Usecase<UpdateProductUsecase.Command, CreateProductUsecase.Result> {

    record Command(
        UUID id,
        UUID sellerId,
        UUID categoryId,
        String name,
        String description,
        BigDecimal price,
        BigDecimal originalPrice,
        List<String> images,
        ProductStatus status
    ) {}
}