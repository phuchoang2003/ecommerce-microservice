package com.hdp.order_service.infrastructure.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import static org.assertj.core.api.Assertions.assertThat;

class BearerTokenRelayInterceptorTest {

    private final RequestInterceptor interceptor = new FeignSecurityConfig().bearerTokenRelay();

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void addsBearerHeaderFromJwtAuthentication() {
        Jwt jwt = Jwt.withTokenValue("token-value")
                .header("alg", "none")
                .subject("user-1")
                .build();
        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(jwt));

        RequestTemplate template = new RequestTemplate();
        interceptor.apply(template);

        assertThat(template.headers().get(HttpHeaders.AUTHORIZATION))
                .containsExactly("Bearer token-value");
    }

    @Test
    void doesNothingForAnonymous() {
        RequestTemplate template = new RequestTemplate();
        interceptor.apply(template);

        assertThat(template.headers()).doesNotContainKey(HttpHeaders.AUTHORIZATION);
    }
}