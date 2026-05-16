package com.hdp.core.exception;

import java.util.Collections;
import java.util.Map;

public class ValidationException extends BusinessException {

    private final Map<String, Object> details;

    public ValidationException(String message) {
        super(ErrorCode.VALIDATION_ERROR, message);
        this.details = Collections.emptyMap();
    }

    public ValidationException(String message, Map<String, Object> details) {
        super(ErrorCode.VALIDATION_ERROR, message);
        this.details = details != null ? details : Collections.emptyMap();
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    @Override
    public String getMessage() {
        if (details.isEmpty()) {
            return super.getMessage();
        }
        return super.getMessage() + " | details=" + details;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ValidationException[")
                .append(getErrorCode())
                .append("]: ")
                .append(super.getMessage());

        if (!details.isEmpty()) {
            sb.append("\nDetails:");
            details.forEach((key, value) -> {
                sb.append("\n - ").append(key).append(": ");

                if (value instanceof Iterable<?> iterable) {
                    boolean first = true;
                    for (Object v : iterable) {
                        if (!first) sb.append(", ");
                        sb.append(v);
                        first = false;
                    }
                } else {
                    sb.append(value);
                }
            });
        }

        return sb.toString();
    }
}