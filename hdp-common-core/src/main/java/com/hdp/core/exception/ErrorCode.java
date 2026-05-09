package com.hdp.core.exception;


import com.hdp.core.constant.BaseMessageKeyConstants;
import com.hdp.core.constant.CodeConstants;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    VALIDATION_ERROR(CodeConstants.E001, BaseMessageKeyConstants.VALIDATION_ERROR),
    NOT_FOUND(CodeConstants.E002, BaseMessageKeyConstants.NOT_FOUND),
    BUSINESS_ERROR(CodeConstants.E003, BaseMessageKeyConstants.BUSINESS_ERROR),
    UNAUTHORIZED(CodeConstants.E004, BaseMessageKeyConstants.UNAUTHORIZED),
    FORBIDDEN(CodeConstants.E005, BaseMessageKeyConstants.FORBIDDEN),
    CONSTRAINT_VIOLATION(CodeConstants.E006, BaseMessageKeyConstants.CONSTRAINT_VIOLATION),
    CONSTRAINT_DECLARATION_ERROR(CodeConstants.E007, BaseMessageKeyConstants.CONSTRAINT_DECLARATION_ERROR),
    INTERNAL_ERROR(CodeConstants.E500, BaseMessageKeyConstants.INTERNAL_ERROR);

    private final String code;
    private final String messageKey;
}