package com.hdp.product_service.application.port.in;


import com.hdp.core.usecase.Usecase;

import java.util.UUID;

public interface DeleteCategoryUsecase extends Usecase<DeleteCategoryUsecase.Command, DeleteCategoryUsecase.Result> {

    record Command(UUID id) {}

    record Result(UUID id, boolean deleted) {}
}