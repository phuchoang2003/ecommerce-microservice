package com.hdp.core.core.test;

import java.util.List;
import java.util.UUID;

public final class TestDataFactory {
    private TestDataFactory() {
    }

    public static String randomId() {
        return UUID.randomUUID().toString();
    }

    public static <T> T notNull(T value) {
        if (value == null) {
            throw new AssertionError("Expected non-null value");
        }
        return value;
    }

    public static <T> List<T> notEmpty(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new AssertionError("Expected non-empty list");
        }
        return list;
    }
}
