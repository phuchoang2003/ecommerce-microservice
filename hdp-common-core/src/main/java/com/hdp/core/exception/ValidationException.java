package com.hdp.core.exception;

import java.util.Map;

public class ValidationException extends BusinessException {
    public ValidationException(String message) {
        super(ErrorCode.VALIDATION_ERROR, message);
    }

    public ValidationException(String message, Map<String, Object> details) {
        super(ErrorCode.VALIDATION_ERROR, message, details);
    }
}