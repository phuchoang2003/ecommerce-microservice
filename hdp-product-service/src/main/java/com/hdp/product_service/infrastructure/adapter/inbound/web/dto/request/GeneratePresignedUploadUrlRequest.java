package com.hdp.product_service.infrastructure.adapter.inbound.web.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hdp.core.constant.BaseMessageKeyConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record GeneratePresignedUploadUrlRequest(
        @NotBlank(message = BaseMessageKeyConstants.VALIDATION_NOT_BLANK)
        @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = BaseMessageKeyConstants.VALIDATION_PATTERN)
        @JsonProperty("file_name")
        String fileName,

        @NotBlank(message = BaseMessageKeyConstants.VALIDATION_NOT_BLANK)
        @JsonProperty("content_type")
        String contentType,

        @NotBlank(message = BaseMessageKeyConstants.VALIDATION_NOT_BLANK)
        String category
) {
}
