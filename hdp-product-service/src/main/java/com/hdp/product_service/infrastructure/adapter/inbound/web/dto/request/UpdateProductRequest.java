package com.hdp.product_service.infrastructure.adapter.inbound.web.dto.request;

import com.hdp.common.web.annotations.EnumPatternValidate;
import com.hdp.core.constant.BaseMessageKeyConstants;
import com.hdp.core.constant.ValidateConstants;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public record UpdateProductRequest(
        @NotBlank(message = BaseMessageKeyConstants.VALIDATION_NOT_BLANK) String sellerId,
        @NotBlank(message = BaseMessageKeyConstants.VALIDATION_NOT_BLANK) String categoryId,
        @NotBlank(message = BaseMessageKeyConstants.VALIDATION_NOT_BLANK) @Size(max = ValidateConstants.MAX_NAME_LENGTH, message = BaseMessageKeyConstants.VALIDATION_SIZE) String name,
        @Size(max = ValidateConstants.MAX_DESCRIPTION_LENGTH, message = BaseMessageKeyConstants.VALIDATION_SIZE) String description,
        @NotNull(message = BaseMessageKeyConstants.VALIDATION_NOT_NULL) @Positive(message = BaseMessageKeyConstants.VALIDATION_POSITIVE) BigDecimal price,
        @Positive(message = BaseMessageKeyConstants.VALIDATION_POSITIVE) BigDecimal originalPrice,
        @Size(max = ValidateConstants.MAX_IMAGES_COUNT, message = BaseMessageKeyConstants.VALIDATION_SIZE) List<@Size(max = ValidateConstants.MAX_IMAGE_URL_LENGTH, message = BaseMessageKeyConstants.VALIDATION_SIZE) String> images,
        @EnumPatternValidate(enumClass = com.hdp.product_service.domain.model.valueobject.ProductStatus.class) String status
) {
}