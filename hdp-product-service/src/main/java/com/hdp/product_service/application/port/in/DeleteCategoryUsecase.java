package com.hdp.product_service.application.port.in;


import com.hdp.core.usecase.Usecase;
import com.hdp.core.validation.SelfValidator;
import com.hdp.core.validation.ValidationResult;

import java.util.UUID;

public interface DeleteCategoryUsecase extends Usecase<DeleteCategoryUsecase.Command, DeleteCategoryUsecase.Result> {

    record Command(UUID id) implements SelfValidator<Command> {

        @Override
        public ValidationResult validate(Command input) {
            ValidationResult result = new ValidationResult();

            if (input.id() == null) {
                result.add("id", "Category ID is required");
            }

            return result;
        }
    }

    record Result(UUID id, boolean deleted) {}
}