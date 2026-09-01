package com.hdp.product_service.application.port.in;

import com.hdp.core.usecase.Usecase;

public interface GeneratePresignedUrlUsecase extends Usecase<GeneratePresignedUrlUsecase.Command, GeneratePresignedUrlUsecase.Result> {

    record Command(
            String fileName,
            String contentType,
            String category
    ) {}

    record Result(
            String uploadUrl,
            String fileKey,
            String fileUrl,
            java.time.Instant expiresAt
    ) {}
}
