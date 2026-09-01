package com.hdp.product_service.filestorage.command;

import java.util.Set;

public record GeneratePresignedUrlCommand(
        String fileName,
        String contentType,
        String category,
        Long maxSizeBytes
) {}