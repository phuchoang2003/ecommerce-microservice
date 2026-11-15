package com.hdp.common.security.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import tools.jackson.databind.json.JsonMapper;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SecurityProblemWriterTest {

    @AfterEach
    void clearMdc() {
        MDC.clear();
    }

    @Test
    void writes401ProblemDetailWithTraceIdAndErrorCode() throws Exception {
        MessageSource messageSource = mock(MessageSource.class);
        when(messageSource.getMessage(anyString(), any(), any(Locale.class))).thenReturn("message");

        MDC.put("trace_id", "trace-1");
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/users/1");
        MockHttpServletResponse response = new MockHttpServletResponse();

        new SecurityProblemWriter(JsonMapper.builder().build(), messageSource)
                .commence(request, response, new InsufficientAuthenticationException("no token"));

        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getContentType()).startsWith("application/problem+json");

        String body = response.getContentAsString();
        assertThat(body).contains("\"error_code\":\"E004\"");
        assertThat(body).contains("\"trace_id\":\"trace-1\"");
        assertThat(body).contains("\"instance\":\"/api/v1/users/1\"");
    }
}