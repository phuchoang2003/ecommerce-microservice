package com.hdp.core.exception;

public class DuplicateKeyBusinessException extends BusinessException {
    public DuplicateKeyBusinessException(String resource, Object key) {
        super(ErrorCode.DUPLICATE_KEY, null, resource, key);
    }

    public DuplicateKeyBusinessException(String resource, Object key, Throwable cause) {
        super(ErrorCode.DUPLICATE_KEY, cause);
        initCause(cause);
    }
}
