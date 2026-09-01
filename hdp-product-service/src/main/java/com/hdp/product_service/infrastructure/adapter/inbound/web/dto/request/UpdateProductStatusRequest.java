package com.hdp.product_service.infrastructure.adapter.inbound.web.dto.request;

import com.hdp.core.constant.BaseMessageKeyConstants;
import com.hdp.product_service.domain.model.valueobject.ProductStatus;
import jakarta.validation.constraints.NotBlank;

public record UpdateProductStatusRequest(
        @NotBlank(message = BaseMessageKeyConstants.VALIDATION_NOT_BLANK) String sellerId,
        ProductStatus newStatus
) {
}