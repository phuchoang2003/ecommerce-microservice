package com.hdp.core.exception;

public class NotFoundException extends BusinessException {
    public NotFoundException(String resource, Object id) {
        super(ErrorCode.NOT_FOUND, null, resource, id);
    }

    public NotFoundException(String resource, Object id, Throwable cause) {
        super(ErrorCode.NOT_FOUND, cause);
        initCause(cause);
    }
}