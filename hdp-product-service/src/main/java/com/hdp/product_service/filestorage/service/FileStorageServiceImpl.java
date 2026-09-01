package com.hdp.product_service.filestorage.service;

import com.hdp.product_service.application.port.in.generatepresignedurl.GeneratePresignedUrlCommand;
import com.hdp.product_service.filestorage.FileStorageService;
import com.hdp.product_service.filestorage.command.DeleteFilesCommand;
import com.hdp.product_service.filestorage.command.DownloadFileCommand;
import com.hdp.product_service.filestorage.result.DeleteFilesResult;
import com.hdp.product_service.filestorage.result.DownloadFileResult;
import com.hdp.product_service.filestorage.properties.AwsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final AwsProperties properties;
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    public FileStorageServiceImpl(AwsProperties properties, S3Client s3Client, S3Presigner s3Presigner) {
        this.properties = properties;
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
    }

    @Override
    public GeneratePresignedUrlResult generatePresignedUploadUrl(GeneratePresignedUrlCommand command) {
        String fileKey = buildFileKey(command.category(), command.fileName());

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(properties.bucket())
                .key(fileKey)
                .contentType(command.contentType())
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(properties.presignedUrlExpirationMinutes()))
                .putObjectRequest(objectRequest)
                .build();

        PresignedPutObjectRequest result = s3Presigner.presignPutObject(presignRequest);

        Instant expiresAt = Instant.now().plus(properties.presignedUrlExpirationMinutes(), ChronoUnit.MINUTES);

        return new GeneratePresignedUrlResult(
                result.url().toString(),
                fileKey,
                buildFileUrl(fileKey),
                expiresAt
        );
    }

    @Override
    public String getFileUrl(String fileKey) {
        return buildFileUrl(fileKey);
    }

    @Override
    public DownloadFileResult downloadFile(DownloadFileCommand command) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(properties.bucket())
                .key(command.fileKey())
                .build();

        ResponseInputStream<GetObjectResponse> response = s3Client.getObject(request);

        return new DownloadFileResult(
                command.fileKey(),
                response.response().contentType(),
                response.response().contentLength(),
                response
        );
    }

    @Override
    public void deleteFile(String fileKey) {
        s3Client.deleteObject(builder -> builder
                .bucket(properties.bucket())
                .key(fileKey)
        );
        log.info("Deleted file: {}", fileKey);
    }

    @Override
    public DeleteFilesResult deleteFiles(DeleteFilesCommand command) {
        List<String> deletedKeys = new ArrayList<>();

        for (String fileKey : command.fileKeys()) {
            try {
                s3Client.deleteObject(builder -> builder
                        .bucket(properties.bucket())
                        .key(fileKey)
                );
                deletedKeys.add(fileKey);
            } catch (Exception e) {
                log.warn("Failed to delete file: {}", fileKey, e);
            }
        }

        int successCount = deletedKeys.size();
        int failureCount = command.fileKeys().size() - successCount;

        return new DeleteFilesResult(deletedKeys, successCount, failureCount);
    }

    private String buildFileKey(String category, String fileName) {
        String uuid = UUID.randomUUID().toString();
        return String.format("%s/%s/%s", category, uuid, fileName);
    }

    private String buildFileUrl(String fileKey) {
        if (properties.publicHost() != null && !properties.publicHost().isEmpty()) {
            return String.format("%s/%s/%s", properties.publicHost(), properties.bucket(), fileKey);
        }
        if (properties.endpoint() != null) {
            return String.format("%s/%s/%s", properties.endpoint(), properties.bucket(), fileKey);
        }
        return String.format("https://%s.s3.%s.amazonaws.com/%s", properties.bucket(), properties.region(), fileKey);
    }
}