package com.hdp.product_service.infrastructure.adapter.inbound.web.dto.request;

import com.hdp.core.constant.BaseMessageKeyConstants;
import com.hdp.core.constant.ValidateConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCategoryRequest(
        @NotBlank(message = BaseMessageKeyConstants.VALIDATION_NOT_BLANK) @Size(max = ValidateConstants.MAX_NAME_LENGTH, message = BaseMessageKeyConstants.VALIDATION_SIZE) String name,
        @Size(max = ValidateConstants.MAX_PARENT_ID_LENGTH, message = BaseMessageKeyConstants.VALIDATION_SIZE) String parentId
) {
}