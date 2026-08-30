package com.hdp.common.infrastructure.info;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

@ConfigurationProperties(prefix = "app.info")
public record ApplicationInfo(String serviceName, String hostname, String environment) {

    private static final String UNKNOWN = "unknown";
    private static final String DEFAULT_ENVIRONMENT = "development";

    public ApplicationInfo {
        if (!StringUtils.hasText(serviceName)) {
            serviceName = UNKNOWN;
        }
        if (!StringUtils.hasText(hostname)) {
            hostname = resolveHostname();
        }
        if (!StringUtils.hasText(environment)) {
            environment = DEFAULT_ENVIRONMENT;
        }
    }

    private static String resolveHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return UNKNOWN;
        }
    }
}
