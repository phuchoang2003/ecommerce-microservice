package com.hdp.product_service.filestorage.result;

import java.time.Instant;

public record GeneratePresignedUrlResult(
        String uploadUrl,
        String fileKey,
        String fileUrl,
        Instant expiresAt
) {}