package com.hdp.product_service.application.port.in;


import com.hdp.core.usecase.Usecase;

import java.util.UUID;

public interface UpdateCategoryUsecase extends Usecase<UpdateCategoryUsecase.Command, CreateCategoryUsecase.Result> {

    record Command(
        UUID id,
        String name,
        UUID parentId
    ) {}
}