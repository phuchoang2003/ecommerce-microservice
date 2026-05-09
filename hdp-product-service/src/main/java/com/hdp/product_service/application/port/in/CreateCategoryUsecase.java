package com.hdp.product_service.application.port.in;

import com.hdp.core.usecase.Usecase;
import com.hdp.core.validation.SelfValidator;
import com.hdp.core.validation.ValidationResult;

import java.time.Instant;
import java.util.UUID;

public interface CreateCategoryUsecase extends Usecase<CreateCategoryUsecase.Command, CreateCategoryUsecase.Result> {

    record Command(
        UUID parentId,
        String name
    ) implements SelfValidator<Command> {

        @Override
        public ValidationResult validate(Command input) {
            ValidationResult result = new ValidationResult();

            if (input.name() == null || input.name().trim().isEmpty()) {
                result.add("name", "Category name is required");
            } else if (input.name().length() > 100) {
                result.add("name", "Category name must not exceed 100 characters");
            }

            return result;
        }
    }

    record Result(
        UUID id,
        UUID parentId,
        String name,
        String path,
        Instant createdAt
    ) {}
}