package com.hdp.product_service.application.usecase;

import com.hdp.product_service.filestorage.FileStorageService;
import com.hdp.product_service.filestorage.command.GeneratePresignedUrlCommand;
import com.hdp.product_service.filestorage.result.GeneratePresignedUrlResult;
import com.hdp.product_service.application.port.in.GeneratePresignedUrlUsecase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeneratePresignedUrlUsecaseImpl implements GeneratePresignedUrlUsecase {

    private final FileStorageService fileStorageService;

    @Override
    public Result execute(Command command) {
        GeneratePresignedUrlCommand storageCommand = new GeneratePresignedUrlCommand(
                command.fileName(),
                command.contentType(),
                command.category(),
                null
        );

        GeneratePresignedUrlResult storageResult = fileStorageService.generatePresignedUploadUrl(storageCommand);

        log.info("Generated presigned URL for file: {}, key: {}", command.fileName(), storageResult.fileKey());

        return new Result(
                storageResult.uploadUrl(),
                storageResult.fileKey(),
                storageResult.fileUrl(),
                storageResult.expiresAt()
        );
    }
}
