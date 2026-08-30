package com.hdp.common.web.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validates that a String field contains a valid name of the specified enum constants.
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * public class CreateOrderRequest {
 *     @EnumPatternValidate(enumClass = OrderStatus.class)
 *     private String status;
 * }
 * }</pre>
 *
 * <p>The enum constant name is matched case-insensitively by default.</p>
 *
 * @see EnumPatternValidator
 */
@Documented
@Constraint(validatedBy = EnumPatternValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumPatternValidate {

    /**
     * The enum class to validate against.
     */
    Class<? extends Enum<?>> enumClass();

    String message() default "";

    /**
     * Groups for validation.
     */
    Class<?>[] groups() default {};

    /**
     * Payload for clients to associate metadata with a constraint.
     */
    Class<? extends Payload>[] payload() default {};
}