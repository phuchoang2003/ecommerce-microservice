package com.hdp.product_service.application.port.in;

import com.hdp.core.usecase.Usecase;
import com.hdp.core.validation.SelfValidator;
import com.hdp.core.validation.ValidationResult;
import com.hdp.product_service.domain.model.valueobject.ProductStatus;

import java.util.UUID;

public interface UpdateProductStatusUsecase extends Usecase<UpdateProductStatusUsecase.Command, CreateProductUsecase.Result> {

    record Command(
        UUID id,
        UUID sellerId,
        ProductStatus newStatus
    ) implements SelfValidator<Command> {

        @Override
        public ValidationResult validate(Command input) {
            ValidationResult result = new ValidationResult();

            if (input.id() == null) {
                result.add("id", "Product ID is required");
            }
            if (input.sellerId() == null) {
                result.add("sellerId", "Seller ID is required");
            }
            if (input.newStatus() == null) {
                result.add("newStatus", "New status is required");
            }

            return result;
        }
    }
}