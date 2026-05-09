package com.hdp.core.util;

public class NamingConvetionUtils {
    public static String toSnakeCase(String input) {
        if (input == null) return null;
        return input.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}