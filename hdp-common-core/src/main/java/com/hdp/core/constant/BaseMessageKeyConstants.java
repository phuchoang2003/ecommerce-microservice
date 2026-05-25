package com.hdp.core.constant;

public final class BaseMessageKeyConstants {

    // Validation
    public static final String VALIDATION_NOT_NULL = "{validation.notNull}";
    public static final String VALIDATION_NOT_EMPTY = "{validation.notEmpty}";
    public static final String VALIDATION_NOT_BLANK = "{validation.notBlank}";
    public static final String VALIDATION_MIN = "{validation.min}";
    public static final String VALIDATION_MAX = "{validation.max}";
    public static final String VALIDATION_SIZE = "{validation.size}";
    public static final String VALIDATION_EMAIL = "{validation.email}";
    public static final String VALIDATION_PATTERN = "{validation.pattern}";
    public static final String VALIDATION_DECIMAL_MIN = "{validation.decimalMin}";
    public static final String VALIDATION_DECIMAL_MAX = "{validation.decimalMax}";
    public static final String VALIDATION_POSITIVE = "{validation.positive}";
    public static final String VALIDATION_POSITIVE_OR_ZERO = "{validation.positiveOrZero}";
    public static final String VALIDATION_NEGATIVE = "{validation.negative}";
    public static final String VALIDATION_NEGATIVE_OR_ZERO = "{validation.negativeOrZero}";
    public static final String VALIDATION_PAST = "{validation.past}";
    public static final String VALIDATION_FUTURE = "{validation.future}";
    public static final String VALIDATION_ENUM_INVALID = "{validation.enum.invalid}";
    public static final String VALIDATION_FILE_CONTENT_TYPE_NOT_ALLOWED = "{validation.file.contentTypeNotAllowed}";
    public static final String VALIDATION_FILE_EXTENSION_NOT_ALLOWED = "{validation.file.extensionNotAllowed}";
    public static final String VALIDATION_FILE_PATH_TRAVERSAL = "{validation.file.pathTraversal}";

    // Error
    public static final String VALIDATION_ERROR = "error.validation";
    public static final String NOT_FOUND = "error.not_found";
    public static final String BUSINESS_ERROR = "error.business";
    public static final String UNAUTHORIZED = "error.unauthorized";
    public static final String FORBIDDEN = "error.forbidden";
    public static final String CONSTRAINT_VIOLATION = "error.constraint_violation";
    public static final String CONSTRAINT_DECLARATION_ERROR = "error.constraint_declaration";
    public static final String INTERNAL_ERROR = "error.internal";

    private BaseMessageKeyConstants() {}
}