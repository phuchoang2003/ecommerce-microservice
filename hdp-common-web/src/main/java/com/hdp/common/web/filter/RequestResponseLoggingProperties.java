package com.hdp.common.web.filter;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app.logging.request")
public record RequestResponseLoggingProperties(
        Boolean enabled,
        List<String> includePatterns,
        List<String> excludePatterns,
        int maxBodyLength,
        Boolean logRequestBody,
        Boolean logResponseBody
) {

    public RequestResponseLoggingProperties {
        enabled = enabled != null ? enabled : Boolean.TRUE;
        includePatterns = includePatterns != null ? includePatterns : List.of("/api/**");
        excludePatterns = excludePatterns != null ? excludePatterns : List.of("/actuator/**", "/swagger/**", "/v3/api-docs/**");
        maxBodyLength = maxBodyLength > 0 ? maxBodyLength : 10000;
        logRequestBody = logRequestBody != null ? logRequestBody : Boolean.TRUE;
        logResponseBody = logResponseBody != null ? logResponseBody : Boolean.TRUE;
    }
}