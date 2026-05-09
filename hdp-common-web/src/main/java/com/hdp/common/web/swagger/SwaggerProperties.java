package com.hdp.common.web.swagger;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.swagger")
public record SwaggerProperties(
        String title,
        String version,
        String description,
        String contactName,
        String contactEmail,
        String licenseName,
        String licenseUrl,
        String serverUrl,
        String serverDescription,
        String propertyNamingStrategy
) {
    public SwaggerProperties {
        if (title == null) title = "HDP E-Commerce API";
        if (version == null) version = "1.0.0";
        if (description == null) description = "API documentation for HDP E-Commerce Microservices";
        if (contactName == null) contactName = "HDP Team";
        if (contactEmail == null) contactEmail = "dev@hdp.com";
        if (licenseName == null) licenseName = "Proprietary";
        if (licenseUrl == null) licenseUrl = "https://hdp.com/license";
        if (serverUrl == null) serverUrl = "/";
        if (serverDescription == null) serverDescription = "Default Server";
        if (propertyNamingStrategy == null) propertyNamingStrategy = "SNAKE_CASE";
    }
}