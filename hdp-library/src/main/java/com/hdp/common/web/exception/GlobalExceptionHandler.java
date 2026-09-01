package com.hdp.common.web.exception;


import com.hdp.core.constant.DelimeterConstants;
import com.hdp.core.constant.RequestContextConstants;
import com.hdp.core.exception.BusinessException;
import com.hdp.core.exception.ErrorCode;
import com.hdp.core.util.NamingConvetionUtils;
import jakarta.validation.ConstraintDeclarationException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@AutoConfiguration
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private ProblemDetail createProblemDetail(ErrorCode errorCode, String message, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(ErrorCodeHttpMapper.toHttpStatus(errorCode), message);
        problemDetail.setType(ErrorCodeHttpMapper.toTypeUri(errorCode));
        problemDetail.setTitle(getTitle(errorCode.getMessageKey()));
        problemDetail.setInstance(getRequestUri(request));
        problemDetail.setProperty(RequestContextConstants.TRACE_ID, MDC.get(RequestContextConstants.TRACE_ID));
        problemDetail.setProperty(RequestContextConstants.ERROR_CODE, errorCode.getCode());
        problemDetail.setProperty(RequestContextConstants.TIMESTAMP, Instant.now().toString());
        return problemDetail;
    }



    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ProblemDetail> handleBusinessException(BusinessException ex, WebRequest request) {
        log.error("Business exception: {} - {}", ex.getErrorCode(), ex.getMessage());

        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(ex.getErrorCode().getMessageKey(), ex.getMessageArgs(), locale);

        ProblemDetail problemDetail = createProblemDetail(ex.getErrorCode(), message, request);

        if (ex.getDetails() != null) {
            Map<String, Object> details = new LinkedHashMap<>(ex.getDetails());
            details.forEach(problemDetail::setProperty);
        }

        return ResponseEntity.status(ErrorCodeHttpMapper.toHttpStatus(ex.getErrorCode())).body(problemDetail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        log.error("Validation exception: {}", ex.getMessage());

        Map<String, Object> errors = new LinkedHashMap<>();
        Locale locale = LocaleContextHolder.getLocale();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String field = NamingConvetionUtils.toSnakeCase(error.getField());
            String message = error.getDefaultMessage();
            Object[] args = error.getArguments();

            // If default message is already resolved (not a value), use it directly
            if (message != null && !message.contains(DelimeterConstants.DOT)) {
                errors.put(field, message);
            } else if (message != null) {
                // Try to resolve from message source
                String resolved = messageSource.getMessage(message, args, locale);
                errors.put(field, resolved);
            } else {
                errors.put(field, "invalid value");
            }
        });

        String message = messageSource.getMessage(ErrorCode.VALIDATION_ERROR.getMessageKey(), null, locale);

        ProblemDetail problemDetail = createProblemDetail(ErrorCode.VALIDATION_ERROR, message, request);
        problemDetail.setProperty(RequestContextConstants.ERROR_DETAIL, errors);

        return ResponseEntity.status(ErrorCodeHttpMapper.toHttpStatus(ErrorCode.VALIDATION_ERROR)).body(problemDetail);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        log.error("Constraint violation: {}", ex.getMessage());

        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(ErrorCode.CONSTRAINT_VIOLATION.getMessageKey(), null, locale);

        ProblemDetail problemDetail = createProblemDetail(ErrorCode.CONSTRAINT_VIOLATION, message, request);

        return ResponseEntity.status(ErrorCodeHttpMapper.toHttpStatus(ErrorCode.CONSTRAINT_VIOLATION)).body(problemDetail);
    }

    @ExceptionHandler(ConstraintDeclarationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintDeclarationException(ConstraintDeclarationException ex, WebRequest request) {
        log.error("Constraint declaration error: {}", ex.getMessage());

        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(ErrorCode.CONSTRAINT_DECLARATION_ERROR.getMessageKey(), null, locale);

        ProblemDetail problemDetail = createProblemDetail(ErrorCode.CONSTRAINT_DECLARATION_ERROR, message, request);

        return ResponseEntity.status(ErrorCodeHttpMapper.toHttpStatus(ErrorCode.CONSTRAINT_DECLARATION_ERROR)).body(problemDetail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(Exception ex, WebRequest request) {
        log.error("Unexpected exception: ", ex);

        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(ErrorCode.INTERNAL_ERROR.getMessageKey(), null, locale);

        ProblemDetail problemDetail = createProblemDetail(ErrorCode.INTERNAL_ERROR, message, request);

        return ResponseEntity.status(ErrorCodeHttpMapper.toHttpStatus(ErrorCode.INTERNAL_ERROR)).body(problemDetail);
    }

    private URI getRequestUri(WebRequest request) {
        String uri = request.getDescription(false).replace("uri=", "");
        return URI.create(uri);
    }

    private String getTitle(String messageKey) {
        return messageSource.getMessage(messageKey + ".title", null, LocaleContextHolder.getLocale());
    }
}