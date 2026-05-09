package com.hdp.product_service.infrastructure.adapter.inbound.web.dto.response;

import java.time.Instant;

public record CategoryResponse(
    String id,
    String parentId,
    String name,
    String path,
    Instant createdAt
) {
}