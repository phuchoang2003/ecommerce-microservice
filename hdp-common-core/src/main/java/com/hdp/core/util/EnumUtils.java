package com.hdp.core.util;


import com.hdp.core.constant.DelimeterConstants;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility for enum operations.
 */
public final class EnumUtils {

    private EnumUtils() {}

    /**
     * Converts a String to an enum value (case-insensitive).
     *
     * @param enumClass the enum class
     * @param value     the string value to convert
     * @param <E>       the enum type
     * @return the enum value
     * @throws IllegalArgumentException if value is not a valid enum constant
     */
    public static <E extends Enum<E>> E fromString(Class<E> enumClass, String value) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid enum value: " + value + ", must be one of: " +
                                Arrays.stream(enumClass.getEnumConstants())
                                        .map(Enum::name)
                                        .collect(Collectors.joining(DelimeterConstants.PIPE))
                ));
    }

    /**
     * Checks if a String is a valid enum constant name (case-insensitive).
     */
    public static <E extends Enum<E>> boolean isValid(Class<E> enumClass, String value) {
        if (value == null) {
            return false;
        }
        Set<String> validNames = Arrays.stream(enumClass.getEnumConstants())
                .map(e -> e.name().toLowerCase())
                .collect(Collectors.toSet());
        return validNames.contains(value.toLowerCase());
    }
}