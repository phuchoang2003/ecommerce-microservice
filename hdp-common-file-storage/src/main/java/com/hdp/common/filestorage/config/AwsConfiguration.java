package com.hdp.common.filestorage.config;

import com.hdp.common.filestorage.properties.AwsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProviderChain;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties(AwsProperties.class)
public class AwsConfiguration {

    private final AwsProperties properties;

    public AwsConfiguration(AwsProperties properties) {
        this.properties = properties;
    }

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider() {

        List<AwsCredentialsProvider> providers = new ArrayList<>();

        // 1. IAM Role (EC2/ECS/EKS/Lambda)
        providers.add(
                InstanceProfileCredentialsProvider.create()
        );

        // 2. Fallback access key
        if (StringUtils.hasText(properties.accessKeyId())
                && StringUtils.hasText(properties.secretAccessKey())) {

            providers.add(
                    StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(
                                    properties.accessKeyId(),
                                    properties.secretAccessKey()
                            )
                    )
            );
        }

        return AwsCredentialsProviderChain.builder()
                .credentialsProviders(providers)
                .reuseLastProviderEnabled(true)
                .build();
    }

    @Bean
    public S3Client s3Client(AwsCredentialsProvider credentialsProvider) {
        var builder = S3Client.builder()
                .region(Region.of(properties.region()))
                .credentialsProvider(credentialsProvider);

        if (properties.endpoint() != null) {
            builder.endpointOverride(properties.endpoint());
        }
        if (properties.pathStyleAccessEnabled()) {
            builder.forcePathStyle(true);
        }

        return builder.build();
    }

    @Bean
    public S3Presigner s3Presigner(AwsCredentialsProvider credentialsProvider) {
        var builder = S3Presigner.builder()
                .region(Region.of(properties.region()))
                .credentialsProvider(credentialsProvider);

        if (properties.endpoint() != null) {
            builder.endpointOverride(properties.endpoint());
        }

        return builder.build();
    }
}