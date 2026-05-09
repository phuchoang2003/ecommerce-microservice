package com.hdp.core.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Map<String, Object> details;
    private final Object[] messageArgs;

    public BusinessException(String messageKey, Object... messageArgs) {
        super(messageKey);
        this.errorCode = ErrorCode.BUSINESS_ERROR;
        this.details = null;
        this.messageArgs = messageArgs;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessageKey());
        this.errorCode = errorCode;
        this.details = null;
        this.messageArgs = null;
    }

    public BusinessException(ErrorCode errorCode, Object... messageArgs) {
        super(errorCode.getMessageKey());
        this.errorCode = errorCode;
        this.details = null;
        this.messageArgs = messageArgs;
    }

    public BusinessException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode.getMessageKey());
        this.errorCode = errorCode;
        this.details = details;
        this.messageArgs = null;
    }

    public BusinessException(ErrorCode errorCode, Map<String, Object> details, Object... messageArgs) {
        super(errorCode.getMessageKey());
        this.errorCode = errorCode;
        this.details = details;
        this.messageArgs = messageArgs;
    }
}