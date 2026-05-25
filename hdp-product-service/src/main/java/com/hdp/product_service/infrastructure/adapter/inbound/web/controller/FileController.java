package com.hdp.product_service.infrastructure.adapter.inbound.web.controller;

import com.hdp.common.web.dto.response.ApiResponse;
import com.hdp.product_service.application.port.in.GeneratePresignedUrlUsecase;
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

    private final GeneratePresignedUrlUsecase generatePresignedUrlUsecase;

    @PostMapping("/presigned-upload")
    public ResponseEntity<ApiResponse<PresignedUploadUrlResponse>> generatePresignedUploadUrl(
            @Valid @RequestBody GeneratePresignedUploadUrlRequest request) {

        GeneratePresignedUrlUsecase.Command command = new GeneratePresignedUrlUsecase.Command(
                request.fileName(),
                request.contentType(),
                request.category()
        );

        GeneratePresignedUrlUsecase.Result result = generatePresignedUrlUsecase.execute(command);

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
