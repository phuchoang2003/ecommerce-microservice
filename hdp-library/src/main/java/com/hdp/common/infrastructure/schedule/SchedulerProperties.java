package com.hdp.common.infrastructure.schedule;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.scheduler")
public record SchedulerProperties(
        int poolSize,
        String threadNamePrefix
) {
    public SchedulerProperties {
        if (poolSize == 0) poolSize = Runtime.getRuntime().availableProcessors();
        if (threadNamePrefix == null || threadNamePrefix.isBlank()) threadNamePrefix = "scheduler-vt-";
    }
}