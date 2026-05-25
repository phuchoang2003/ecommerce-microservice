package com.hdp.product_service.infrastructure.adapter.inbound.web.dto.response;

import java.time.Instant;

public record PresignedUploadUrlResponse(
        String uploadUrl,
        String fileKey,
        String fileUrl,
        Instant expiresAt
) {
}
