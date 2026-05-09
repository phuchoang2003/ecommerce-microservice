package com.hdp.product_service.application.port.in;


import com.hdp.core.usecase.Usecase;
import com.hdp.core.validation.SelfValidator;
import com.hdp.core.validation.ValidationResult;

import java.util.UUID;

public interface DeleteProductUsecase extends Usecase<DeleteProductUsecase.Command, DeleteProductUsecase.Result> {

    record Command(UUID id, UUID sellerId) implements SelfValidator<Command> {

        @Override
        public ValidationResult validate(Command input) {
            ValidationResult result = new ValidationResult();

            if (input.id() == null) {
                result.add("id", "Product ID is required");
            }
            if (input.sellerId() == null) {
                result.add("sellerId", "Seller ID is required");
            }

            return result;
        }
    }

    record Result(UUID id, boolean deleted) {}
}