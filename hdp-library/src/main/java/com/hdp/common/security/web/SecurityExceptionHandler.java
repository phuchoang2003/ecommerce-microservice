package com.hdp.common.security.web;

import com.hdp.common.web.exception.ErrorCodeHttpMapper;
import com.hdp.core.constant.RequestContextConstants;
import com.hdp.core.exception.CoreErrorCode;
import com.hdp.core.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.time.Instant;
import java.util.Locale;

/**
 * Handles authorization failures thrown by {@code @EnableMethodSecurity} inside MVC.
 *
 * <p>Separate from {@code GlobalExceptionHandler} on purpose: that class is loaded by
 * services without Spring Security on the classpath, and a security import there
 * would break their startup.</p>
 */
@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityExceptionHandler {

    private final MessageSource messageSource;

    public SecurityExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
        log.warn("Access denied: {}", ex.getMessage());

        Locale locale = LocaleContextHolder.getLocale();
        CoreErrorCode errorCode = CoreErrorCode.FORBIDDEN;

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                ErrorCodeHttpMapper.toHttpStatus(errorCode),
                messageSource.getMessage(errorCode.getMessageKey(), null, locale));
        problemDetail.setType(ErrorCodeHttpMapper.toTypeUri(errorCode));
        problemDetail.setTitle(messageSource.getMessage(errorCode.getMessageKey() + ".title", null, locale));
        problemDetail.setInstance(requestUri(request));
        problemDetail.setProperty(RequestContextConstants.TRACE_ID, MDC.get(RequestContextConstants.TRACE_ID));
        problemDetail.setProperty(RequestContextConstants.ERROR_CODE, errorCode.getCode());
        problemDetail.setProperty(RequestContextConstants.TIMESTAMP, Instant.now().toString());

        return ResponseEntity.status(ErrorCodeHttpMapper.toHttpStatus(errorCode)).body(problemDetail);
    }

    private URI requestUri(WebRequest request) {
        return URI.create(request.getDescription(false).replace("uri=", ""));
    }
}