package com.hdp.product_service.application.port.in.listcategories;

import java.time.Instant;
import java.util.UUID;

public record CategoryItem(
    UUID id,
    UUID parentId,
    String name,
    String path,
    Instant createdAt
) {
}
