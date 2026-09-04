package com.hdp.common.web.exception;


import com.hdp.core.exception.CoreErrorCode;
import com.hdp.core.exception.ErrorCode;
import org.springframework.http.HttpStatus;

import java.net.URI;

/**
 * Maps domain ErrorCode to HTTP concerns.
 *
 * <p>Separates domain (business errors) from infrastructure (HTTP concerns).</p>
 *
 * <p>Usage:</p>
 * <pre>{@code
 * ErrorCode code = CoreErrorCode.NOT_FOUND;
 * HttpStatus status = ErrorCodeHttpMapper.toHttpStatus(code);  // HttpStatus.NOT_FOUND
 * URI typeUri = ErrorCodeHttpMapper.toTypeUri(code);  // URI for problem details
 * }</pre>
 */
public final class ErrorCodeHttpMapper {

    private static final String PROBLEM_URI_BASE = "https://example.com/probs/";

    private ErrorCodeHttpMapper() {}

    public static HttpStatus toHttpStatus(ErrorCode code) {
        if (code instanceof CoreErrorCode ec) {
            return switch (ec) {
                case VALIDATION_ERROR, BUSINESS_ERROR, CONSTRAINT_VIOLATION, CONSTRAINT_DECLARATION_ERROR -> HttpStatus.BAD_REQUEST;
                case NOT_FOUND -> HttpStatus.NOT_FOUND;
                case UNAUTHORIZED -> HttpStatus.UNAUTHORIZED;
                case FORBIDDEN -> HttpStatus.FORBIDDEN;
                case DUPLICATE_KEY -> HttpStatus.CONFLICT;
                case INTERNAL_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
            };
        }
        return HttpStatus.BAD_REQUEST;
    }

    public static URI toTypeUri(ErrorCode code) {
        if (code instanceof CoreErrorCode ec) {
            String typeName = switch (ec) {
                case VALIDATION_ERROR -> "validation";
                case NOT_FOUND -> "not-found";
                case BUSINESS_ERROR -> "business";
                case UNAUTHORIZED -> "unauthorized";
                case FORBIDDEN -> "forbidden";
                case CONSTRAINT_VIOLATION -> "constraint-violation";
                case CONSTRAINT_DECLARATION_ERROR -> "constraint-declaration";
                case DUPLICATE_KEY -> "duplicate-key";
                case INTERNAL_ERROR -> "internal";
            };
            return URI.create(PROBLEM_URI_BASE + typeName);
        }
        return URI.create(PROBLEM_URI_BASE + "business");
    }
}