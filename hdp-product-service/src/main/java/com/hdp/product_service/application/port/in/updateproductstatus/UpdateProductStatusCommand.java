package com.hdp.product_service.application.port.in.updateproductstatus;

import com.hdp.product_service.domain.model.valueobject.ProductStatus;

import java.util.UUID;

public record UpdateProductStatusCommand(
    UUID id,
    UUID sellerId,
    ProductStatus newStatus
) {
}
