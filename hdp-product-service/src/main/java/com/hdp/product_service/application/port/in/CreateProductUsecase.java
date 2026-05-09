package com.hdp.product_service.application.port.in;

import com.hdp.core.usecase.Usecase;
import com.hdp.core.validation.SelfValidator;
import com.hdp.core.validation.ValidationResult;
import com.hdp.product_service.domain.model.valueobject.ProductStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface CreateProductUsecase extends Usecase<CreateProductUsecase.Command, CreateProductUsecase.Result> {

    record Command(
        UUID sellerId,
        UUID categoryId,
        String name,
        String description,
        BigDecimal price,
        BigDecimal originalPrice,
        List<String> images,
        ProductStatus status
    ) implements SelfValidator<Command> {

        @Override
        public ValidationResult validate(Command input) {
            ValidationResult result = new ValidationResult();

            if (input.sellerId() == null) {
                result.add("sellerId", "Seller ID is required");
            }
            if (input.categoryId() == null) {
                result.add("categoryId", "Category ID is required");
            }
            if (input.name() == null || input.name().trim().isEmpty()) {
                result.add("name", "Product name is required");
            } else if (input.name().length() > 200) {
                result.add("name", "Product name must not exceed 200 characters");
            }
            if (input.description() != null && input.description().length() > 5000) {
                result.add("description", "Description must not exceed 5000 characters");
            }
            if (input.price() == null || input.price().compareTo(BigDecimal.ZERO) <= 0) {
                result.add("price", "Price must be greater than zero");
            }
            if (input.images() == null || input.images().isEmpty()) {
                result.add("images", "At least one product image is required");
            } else if (input.images().size() > 9) {
                result.add("images", "Maximum 9 product images allowed");
            }
            if (input.status() == null) {
                result.add("status", "Status is required");
            }

            return result;
        }
    }

    record Result(
        UUID id,
        UUID sellerId,
        UUID categoryId,
        String name,
        String description,
        BigDecimal price,
        BigDecimal originalPrice,
        List<String> images,
        BigDecimal rating,
        int soldCount,
        ProductStatus status,
        Instant createdAt,
        Instant updatedAt
    ) {}
}