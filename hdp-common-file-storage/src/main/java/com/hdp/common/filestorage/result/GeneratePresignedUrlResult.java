package com.hdp.common.filestorage.result;

import java.time.Instant;

public record GeneratePresignedUrlResult(
        String uploadUrl,
        String fileKey,
        String fileUrl,
        Instant expiresAt
) {}