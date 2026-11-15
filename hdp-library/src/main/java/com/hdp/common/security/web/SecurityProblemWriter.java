package com.hdp.common.security.web;

import com.hdp.common.web.exception.ErrorCodeHttpMapper;
import com.hdp.core.constant.RequestContextConstants;
import com.hdp.core.exception.CoreErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Writes 401/403 in the same flat problem+json shape as {@code GlobalExceptionHandler}.
 */
@Slf4j
public class SecurityProblemWriter implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final JsonMapper jsonMapper;
    private final MessageSource messageSource;

    public SecurityProblemWriter(JsonMapper jsonMapper, MessageSource messageSource) {
        this.jsonMapper = jsonMapper;
        this.messageSource = messageSource;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException exception) throws IOException {
        log.warn("Unauthorized request to {}: {}", request.getRequestURI(), exception.getMessage());
        write(request, response, CoreErrorCode.UNAUTHORIZED);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException exception) throws IOException {
        log.warn("Access denied for {}: {}", request.getRequestURI(), exception.getMessage());
        write(request, response, CoreErrorCode.FORBIDDEN);
    }

    private void write(HttpServletRequest request, HttpServletResponse response, CoreErrorCode errorCode)
            throws IOException {
        HttpStatus status = ErrorCodeHttpMapper.toHttpStatus(errorCode);
        Locale locale = LocaleContextHolder.getLocale();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("type", ErrorCodeHttpMapper.toTypeUri(errorCode).toString());
        body.put("title", messageSource.getMessage(errorCode.getMessageKey() + ".title", null, locale));
        body.put("status", status.value());
        body.put("detail", messageSource.getMessage(errorCode.getMessageKey(), null, locale));
        body.put("instance", request.getRequestURI());
        body.put(RequestContextConstants.TRACE_ID, MDC.get(RequestContextConstants.TRACE_ID));
        body.put(RequestContextConstants.ERROR_CODE, errorCode.getCode());
        body.put(RequestContextConstants.TIMESTAMP, Instant.now().toString());

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getOutputStream().write(jsonMapper.writeValueAsBytes(body));
    }
}