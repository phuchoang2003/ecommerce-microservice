package com.hdp.product_service.application.port.in;


import com.hdp.core.usecase.Usecase;

import java.util.UUID;

public interface DeleteProductUsecase extends Usecase<DeleteProductUsecase.Command, DeleteProductUsecase.Result> {

    record Command(UUID id, UUID sellerId) {}

    record Result(UUID id, boolean deleted) {}
}