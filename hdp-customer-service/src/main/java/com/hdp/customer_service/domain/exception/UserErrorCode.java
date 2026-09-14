package com.hdp.customer_service.domain.exception;

public enum UserErrorCode {
    USER_EMAIL_DUPLICATE(
            "USER_EMAIL_DUPLICATE",
            "user.email.duplicate"
    ),
    USER_NOT_FOUND(
            "USER_NOT_FOUND",
            "user.not.found"
    ),
    USER_ALREADY_DELETED(
            "USER_ALREADY_DELETED",
            "user.already.deleted"
    );

    private final String code;
    private final String messageKey;

    UserErrorCode(String code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }
}
