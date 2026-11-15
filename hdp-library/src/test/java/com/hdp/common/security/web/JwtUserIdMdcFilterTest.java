package com.hdp.common.security.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUserIdMdcFilterTest {

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
        MDC.clear();
    }

    @Test
    void putsSubjectAndClearsAfterChain() throws Exception {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .subject("keycloak-user-1")
                .build();
        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(jwt));

        AtomicReference<String> userIdInChain = new AtomicReference<>();
        new JwtUserIdMdcFilter().doFilter(
                new MockHttpServletRequest("GET", "/api/v1/users"),
                new MockHttpServletResponse(),
                (request, response) -> userIdInChain.set(MDC.get("user_id")));

        assertThat(userIdInChain.get()).isEqualTo("keycloak-user-1");
        assertThat(MDC.get("user_id")).isNull();
    }
}