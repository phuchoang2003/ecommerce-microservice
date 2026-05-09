package com.hdp.common.web.cors;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;


@ConfigurationProperties(prefix = "app.cors")
public record CorsProperties(
        List<String> allowedOrigins,
        List<String> allowedMethods,
        List<String> allowedHeaders,
        List<String> exposedHeaders,
        Boolean allowCredentials,
        Long maxAge,
        String mapping
) {

    public CorsProperties {
        allowedOrigins = allowedOrigins != null ? allowedOrigins : List.of("*");
        allowedMethods = allowedMethods != null ? allowedMethods : List.of("GET", "POST", "PUT", "DELETE");
        allowedHeaders = allowedHeaders != null ? allowedHeaders : List.of("*");
        exposedHeaders = exposedHeaders != null ? exposedHeaders : List.of();
        allowCredentials = allowCredentials != null ? allowCredentials : Boolean.TRUE;
        maxAge = maxAge != null ? maxAge : 3600L;
        mapping = mapping != null ? mapping : "/api/**";
    }
}