package com.hdp.observability.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.boot.micrometer.metrics.autoconfigure.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Configuration for common tags applied to all metrics.
 * These tags help identify metric sources across services.
 */
@AutoConfiguration
@EnableConfigurationProperties(CommonTagsProperties.class)
public class CommonTagsConfig {

    private final CommonTagsProperties properties;

    public CommonTagsConfig(CommonTagsProperties properties) {
        this.properties = properties;
    }

    /**
     * Customize MeterRegistry to add common tags to all metrics.
     */
    @Bean
    public MeterRegistryCustomizer<MeterRegistry> commonTags() {
        return registry -> registry.config()
            .commonTags(
                "application", properties.application(),
                "environment", properties.environment(),
                "version", properties.version(),
                "host", getHostname()
            );
    }

    /**
     * Add meter filters for renaming, ignoring, or transforming metrics.
     */
    @Bean
    public MeterRegistryCustomizer<MeterRegistry> meterFilters() {
        return registry -> registry.config()
            // Ignore metrics we do not need
//            .meterFilter(MeterFilter.deny(id ->
//                id.getName().startsWith("jvm.gc.pause")))
//
//            // Rename metrics for consistency
//            .meterFilter(MeterFilter.renameTag("http.server.requests",
//                "status", "http_status"))

            // Limit cardinality of URI tag
            .meterFilter(MeterFilter.maximumAllowableTags(
                "http.server.requests", "uri", 100,
                MeterFilter.deny()));
    }

    private String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "unknown";
        }
    }
}
