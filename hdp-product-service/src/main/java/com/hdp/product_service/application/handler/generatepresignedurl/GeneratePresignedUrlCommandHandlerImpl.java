package com.hdp.product_service.application.handler.generatepresignedurl;

import com.hdp.product_service.application.port.in.generatepresignedurl.GeneratePresignedUrlCommand;
import com.hdp.product_service.application.port.in.generatepresignedurl.GeneratePresignedUrlCommandHandler;
import com.hdp.product_service.application.port.in.generatepresignedurl.GeneratePresignedUrlResult;
import com.hdp.product_service.filestorage.FileStorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeneratePresignedUrlCommandHandlerImpl implements GeneratePresignedUrlCommandHandler {

    private final FileStorageService fileStorageService;

    @Override
    public GeneratePresignedUrlResult handle(GeneratePresignedUrlCommand command) {
        GeneratePresignedUrlCommand storageCommand = new GeneratePresignedUrlCommand(
                command.fileName(),
                command.contentType(),
                command.category()
        );

        GeneratePresignedUrlResult storageResult = fileStorageService.generatePresignedUploadUrl(storageCommand);

        log.info("Generated presigned URL for file: {}, key: {}", command.fileName(), storageResult.fileKey());

        return new GeneratePresignedUrlResult(
                storageResult.uploadUrl(),
                storageResult.fileKey(),
                storageResult.fileUrl(),
                storageResult.expiresAt()
        );
    }
}
