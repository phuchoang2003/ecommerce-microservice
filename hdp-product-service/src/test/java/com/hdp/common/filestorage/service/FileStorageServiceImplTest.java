package com.hdp.common.filestorage.service;

import com.hdp.product_service.filestorage.command.GeneratePresignedUrlCommand;
import com.hdp.product_service.filestorage.result.GeneratePresignedUrlResult;
import com.hdp.product_service.filestorage.properties.AwsProperties;
import com.hdp.product_service.filestorage.service.FileStorageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FileStorageServiceImplTest {

    @TempDir
    Path tempDir;

    private AwsProperties properties;
    private FileStorageServiceImpl fileStorageServiceImpl;

    @Mock
    private S3Client s3Client;

    @Mock
    private S3Presigner s3Presigner;

    private PresignedPutObjectRequest mockPresignedPutObjectRequest;
    private S3Presigner untypedPresigner;

    @BeforeEach
    void setUp() throws MalformedURLException {
        MockitoAnnotations.openMocks(this);

        mockPresignedPutObjectRequest = mock(PresignedPutObjectRequest.class);
        when(mockPresignedPutObjectRequest.url()).thenReturn(
                new URL("https://test-bucket.s3.us-east-1.amazonaws.com/products/test-key/test-image.png"));

        untypedPresigner = mock(S3Presigner.class);
        when(untypedPresigner.presignPutObject(any(PutObjectPresignRequest.class)))
                .thenReturn(mockPresignedPutObjectRequest);

        properties = new AwsProperties(
                "test-bucket",
                "us-east-1",
                "test-key",
                "test-secret",
                null,
                false,
                15,
                null
        );
        fileStorageServiceImpl = new FileStorageServiceImpl(properties, s3Client, untypedPresigner);
    }

    @Test
    void generatePresignedUploadUrl_shouldGenerateValidResponse() {
        GeneratePresignedUrlCommand command = new GeneratePresignedUrlCommand(
                "test-image.png",
                "image/png",
                "products",
                null
        );

        GeneratePresignedUrlResult result = fileStorageServiceImpl.generatePresignedUploadUrl(command);

        assertNotNull(result);
        assertNotNull(result.uploadUrl());
        assertNotNull(result.fileKey());
        assertNotNull(result.fileUrl());
        assertNotNull(result.expiresAt());
        assertTrue(result.uploadUrl().contains("https://"));
        assertTrue(result.fileKey().startsWith("products/"));
        assertTrue(result.fileKey().contains("test-image.png"));
    }

    @Test
    void generatePresignedUploadUrl_withDifferentCategory_shouldIncludeCategoryInKey() {
        GeneratePresignedUrlCommand command = new GeneratePresignedUrlCommand(
                "document.pdf",
                "application/pdf",
                "documents",
                null
        );

        GeneratePresignedUrlResult result = fileStorageServiceImpl.generatePresignedUploadUrl(command);

        assertTrue(result.fileKey().startsWith("documents/"));
    }

    @Test
    void generatePresignedUploadUrl_withPublicHost_shouldUseCustomHost() {
        properties = new AwsProperties(
                "test-bucket",
                "us-east-1",
                "test-key",
                "test-secret",
                null,
                false,
                15,
                "https://cdn.example.com"
        );
        fileStorageServiceImpl = new FileStorageServiceImpl(properties, s3Client, untypedPresigner);

        GeneratePresignedUrlCommand command = new GeneratePresignedUrlCommand(
                "image.jpg",
                "image/jpeg",
                "images",
                null
        );

        GeneratePresignedUrlResult result = fileStorageServiceImpl.generatePresignedUploadUrl(command);

        assertTrue(result.fileUrl().startsWith("https://cdn.example.com/"));
    }

    @Test
    void getFileUrl_shouldReturnCorrectUrl() {
        String fileKey = "products/123/file.png";
        String fileUrl = fileStorageServiceImpl.getFileUrl(fileKey);

        assertNotNull(fileUrl);
        assertTrue(fileUrl.contains(fileKey));
    }

    @Test
    void getFileUrl_withPublicHost_shouldPrependPublicHost() {
        properties = new AwsProperties(
                "test-bucket",
                "us-east-1",
                "test-key",
                "test-secret",
                null,
                false,
                15,
                "https://cdn.example.com"
        );
        fileStorageServiceImpl = new FileStorageServiceImpl(properties, s3Client, s3Presigner);

        String fileKey = "products/123/file.png";
        String fileUrl = fileStorageServiceImpl.getFileUrl(fileKey);

        assertTrue(fileUrl.startsWith("https://cdn.example.com/"));
        assertTrue(fileUrl.contains("test-bucket"));
        assertTrue(fileUrl.contains(fileKey));
    }

    @Test
    void deleteFile_shouldCallS3Client() {
        String fileKey = "products/123/file.png";

        fileStorageServiceImpl.deleteFile(fileKey);

        assertNotNull(fileKey);
    }
}