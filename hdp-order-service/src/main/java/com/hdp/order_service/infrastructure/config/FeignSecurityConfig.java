package com.hdp.order_service.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import feign.RequestInterceptor;

/**
 * Relays the caller's bearer token to downstream services.
 *
 * <p>Only works on request threads; Kafka-driven flows have no user token.</p>
 */
@Configuration
public class FeignSecurityConfig {

    @Bean
    public RequestInterceptor bearerTokenRelay() {
        return template -> {
            if (SecurityContextHolder.getContext().getAuthentication()
                    instanceof JwtAuthenticationToken authentication) {
                template.header(HttpHeaders.AUTHORIZATION,
                        "Bearer " + authentication.getToken().getTokenValue());
            }
        };
    }
}