package com.hdp.product_service.application.port.in.getcategory;

import java.time.Instant;
import java.util.UUID;

public record GetCategoryResult(
    UUID id,
    UUID parentId,
    String name,
    String path,
    Instant createdAt
) {
}
