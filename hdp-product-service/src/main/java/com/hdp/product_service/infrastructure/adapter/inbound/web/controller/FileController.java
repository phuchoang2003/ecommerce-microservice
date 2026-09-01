package com.hdp.product_service.infrastructure.adapter.inbound.web.controller;

import com.hdp.common.web.dto.response.ApiResponse;
import com.hdp.product_service.application.port.in.generatepresignedurl.GeneratePresignedUrlCommand;
import com.hdp.product_service.application.port.in.generatepresignedurl.GeneratePresignedUrlCommandHandler;
import com.hdp.product_service.application.port.in.generatepresignedurl.GeneratePresignedUrlResult;
import com.hdp.product_service.infrastructure.adapter.inbound.web.dto.request.GeneratePresignedUploadUrlRequest;
import com.hdp.product_service.infrastructure.adapter.inbound.web.dto.response.PresignedUploadUrlResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final GeneratePresignedUrlCommandHandler generatePresignedUrlCommandHandler;

    @PostMapping("/presigned-upload")
    public ResponseEntity<ApiResponse<PresignedUploadUrlResponse>> generatePresignedUploadUrl(
            @Valid @RequestBody GeneratePresignedUploadUrlRequest request) {

        GeneratePresignedUrlCommand command = new GeneratePresignedUrlCommand(
                request.fileName(),
                request.contentType(),
                request.category()
        );

        GeneratePresignedUrlResult result = generatePresignedUrlCommandHandler.handle(command);

        PresignedUploadUrlResponse response = new PresignedUploadUrlResponse(
                result.uploadUrl(),
                result.fileKey(),
                result.fileUrl(),
                result.expiresAt()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(response, "Presigned URL generated successfully"));
    }
}
