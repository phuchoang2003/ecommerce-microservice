package com.hdp.product_service.application.port.in;


import com.hdp.core.usecase.Usecase;
import com.hdp.core.validation.SelfValidator;
import com.hdp.core.validation.ValidationResult;

import java.util.UUID;

public interface UpdateCategoryUsecase extends Usecase<UpdateCategoryUsecase.Command, CreateCategoryUsecase.Result> {

    record Command(
        UUID id,
        String name,
        UUID parentId
    ) implements SelfValidator<Command> {

        @Override
        public ValidationResult validate(Command input) {
            ValidationResult result = new ValidationResult();

            if (input.id() == null) {
                result.add("id", "Category ID is required");
            }
            if (input.name() != null && input.name().length() > 100) {
                result.add("name", "Category name must not exceed 100 characters");
            }

            return result;
        }
    }
}