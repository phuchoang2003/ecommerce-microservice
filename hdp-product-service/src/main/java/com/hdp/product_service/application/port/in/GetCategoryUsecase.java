package com.hdp.product_service.application.port.in;


import com.hdp.core.usecase.Usecase;

import java.util.UUID;

public interface GetCategoryUsecase extends Usecase<GetCategoryUsecase.Command, CreateCategoryUsecase.Result> {

    record Command(UUID id) {}
}