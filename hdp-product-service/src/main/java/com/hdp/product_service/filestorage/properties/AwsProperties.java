package com.hdp.product_service.filestorage.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@ConfigurationProperties(prefix = "hdp.aws")
public record AwsProperties(
        String bucket,
        String region,
        String accessKeyId,
        String secretAccessKey,
        URI endpoint,
        boolean pathStyleAccessEnabled,
        int presignedUrlExpirationMinutes,
        String publicHost
) {
    public AwsProperties {
        if (bucket == null || bucket.isEmpty()) {
            throw new IllegalArgumentException("bucket is required");
        }
        if (region == null || region.isEmpty()) {
            throw new IllegalArgumentException("region is required");
        }
        if (accessKeyId == null || accessKeyId.isEmpty()) {
            throw new IllegalArgumentException("accessKeyId is required");
        }
        if (secretAccessKey == null || secretAccessKey.isEmpty()) {
            throw new IllegalArgumentException("secretAccessKey is required");
        }
        if (presignedUrlExpirationMinutes <= 0) {
            throw new IllegalArgumentException("presignedUrlExpirationMinutes must be greater than zero");
        }
    }
}