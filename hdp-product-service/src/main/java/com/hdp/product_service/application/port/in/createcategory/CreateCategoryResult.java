package com.hdp.product_service.application.port.in.createcategory;

import java.time.Instant;
import java.util.UUID;

public record CreateCategoryResult(
    UUID id,
    UUID parentId,
    String name,
    String path,
    Instant createdAt
) {
}
