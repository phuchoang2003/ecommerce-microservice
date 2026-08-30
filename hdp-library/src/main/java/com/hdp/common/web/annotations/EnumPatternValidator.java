package com.hdp.common.web.annotations;

import com.hdp.core.constant.BaseMessageKeyConstants;
import com.hdp.core.constant.DelimeterConstants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Validator for {@link EnumPatternValidate} annotation.
 * Validates that a String value matches an enum constant name (case-insensitive).
 */
public class EnumPatternValidator implements ConstraintValidator<EnumPatternValidate, String> {
    private final MessageSource messageSource;
    private Set<String> validValues;
    private String enumValues;

    public EnumPatternValidator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public void initialize(EnumPatternValidate annotation) {
        validValues = Arrays.stream(annotation.enumClass().getEnumConstants()).map(Enum::name).map(String::toLowerCase).collect(Collectors.toSet());
        enumValues = Arrays.stream(annotation.enumClass().getEnumConstants()).map(Enum::name).collect(Collectors.joining(DelimeterConstants.PIPE));
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Use @NotNull for null checks
        }
        if (!validValues.contains(value.toLowerCase())) {
            String message = messageSource.getMessage(
                    BaseMessageKeyConstants.VALIDATION_ENUM_INVALID,
                    new Object[]{enumValues},
                    LocaleContextHolder.getLocale()
            );
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
            return false;
        }
        return true;
    }
}