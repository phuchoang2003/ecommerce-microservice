package com.hdp.common.infrastructure.executor;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.cpu-pool")
public record CpuPoolProperties(
        int coreSize,
        int maxSize,
        int queueCapacity,
        long keepAliveSeconds
) {
    public CpuPoolProperties {
        if (coreSize == 0) coreSize = Runtime.getRuntime().availableProcessors();
        if (maxSize == 0) maxSize = Runtime.getRuntime().availableProcessors();
        if (queueCapacity == 0) queueCapacity = 50;
        if (keepAliveSeconds == 0) keepAliveSeconds = 60;
    }
}