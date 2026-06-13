package com.hdp.observability.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.observability")
public record CommonTagsProperties(
        String application,
        String environment,
        String version
) {

    public CommonTagsProperties {
        if (application == null || application.isBlank()) {
            application = "unknown";
        }
        if (environment == null || environment.isBlank()) {
            environment = "development";
        }
        if (version == null || version.isBlank()) {
            version = "unknown";
        }
    }
}
