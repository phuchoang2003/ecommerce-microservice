package com.hdp.product_service.application.port.in.generatepresignedurl;

import java.time.Instant;

public record GeneratePresignedUrlResult(
    String uploadUrl,
    String fileKey,
    String fileUrl,
    Instant expiresAt
) {
}
