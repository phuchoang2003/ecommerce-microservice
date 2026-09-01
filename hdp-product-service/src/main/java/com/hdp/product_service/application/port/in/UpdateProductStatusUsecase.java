package com.hdp.product_service.application.port.in;

import com.hdp.core.usecase.Usecase;
import com.hdp.product_service.domain.model.valueobject.ProductStatus;

import java.util.UUID;

public interface UpdateProductStatusUsecase extends Usecase<UpdateProductStatusUsecase.Command, CreateProductUsecase.Result> {

    record Command(
        UUID id,
        UUID sellerId,
        ProductStatus newStatus
    ) {}
}