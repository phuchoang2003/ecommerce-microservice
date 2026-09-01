package com.hdp.product_service.application.port.in.generatepresignedurl;

public record GeneratePresignedUrlCommand(
    String fileName,
    String contentType,
    String category
) {
}
