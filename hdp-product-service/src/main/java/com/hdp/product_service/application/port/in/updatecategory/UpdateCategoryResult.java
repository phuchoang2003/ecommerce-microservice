package com.hdp.product_service.application.port.in.updatecategory;

import java.time.Instant;
import java.util.UUID;

public record UpdateCategoryResult(
    UUID id,
    UUID parentId,
    String name,
    String path,
    Instant createdAt
) {
}
