package com.hdp.core.exception;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException() {
        super(CoreErrorCode.UNAUTHORIZED);
    }

    public UnauthorizedException(String message) {
        super(CoreErrorCode.UNAUTHORIZED, message);
    }
}