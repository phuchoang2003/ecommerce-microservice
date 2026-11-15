package com.hdp.common.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

@ConfigurationProperties(prefix = "app.security")
public record HdpSecurityProperties(
        Boolean enabled,
        String issuerUri,
        String jwkSetUri,
        List<String> permitAll,
        List<String> permitAllGet,
        List<String> permitAllPost,
        Keycloak keycloak,
        Auth auth) {

    public HdpSecurityProperties {
        if (enabled == null) enabled = true;
        if (permitAll == null) permitAll = List.of();
        if (permitAllGet == null) permitAllGet = List.of();
        if (permitAllPost == null) permitAllPost = List.of();
        if (keycloak == null) keycloak = new Keycloak(null, null, null, null, null, null, null);
        if (auth == null) auth = new Auth(null);
    }

    public record Keycloak(
            String baseUrl,
            String realm,
            String clientId,
            String clientSecret,
            Duration connectTimeout,
            Duration readTimeout,
            String scope) {

        public Keycloak {
            if (baseUrl == null) baseUrl = "http://localhost:8282";
            if (realm == null) realm = "hdp";
            if (clientId == null) clientId = "hdp-backend";
            if (connectTimeout == null) connectTimeout = Duration.ofSeconds(2);
            if (readTimeout == null) readTimeout = Duration.ofSeconds(5);
            if (scope == null) scope = "openid";
        }

        public String tokenUrl() {
            return baseUrl + "/realms/" + realm + "/protocol/openid-connect/token";
        }

        public String logoutUrl() {
            return baseUrl + "/realms/" + realm + "/protocol/openid-connect/logout";
        }
    }

    public record Auth(Boolean enabled) {
        public Auth {
            if (enabled == null) enabled = false;
        }
    }
}