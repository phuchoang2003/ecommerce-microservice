package com.hdp.product_service.application.port.in;


import com.hdp.core.usecase.Usecase;

import java.util.UUID;

public interface GetProductUsecase extends Usecase<GetProductUsecase.Command, CreateProductUsecase.Result> {

    record Command(UUID id) {
    }
}