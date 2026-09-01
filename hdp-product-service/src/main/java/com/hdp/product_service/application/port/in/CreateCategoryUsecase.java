package com.hdp.product_service.application.port.in;

import com.hdp.core.usecase.Usecase;

import java.time.Instant;
import java.util.UUID;

public interface CreateCategoryUsecase extends Usecase<CreateCategoryUsecase.Command, CreateCategoryUsecase.Result> {

    record Command(
        UUID parentId,
        String name
    ) {}

    record Result(
        UUID id,
        UUID parentId,
        String name,
        String path,
        Instant createdAt
    ) {}
}