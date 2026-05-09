package com.hdp.core.request;


public record SortItem(
    String field,
    SortDirection direction
) {
    public SortItem {
        if (field == null || field.isBlank()) {
            throw new IllegalArgumentException("Field cannot be null or blank");
        }
        if (direction == null) {
            direction = SortDirection.ASC;
        }
    }

    public static SortItem of(String field, SortDirection direction) {
        return new SortItem(field, direction);
    }

    public static SortItem asc(String field) {
        return new SortItem(field, SortDirection.ASC);
    }

    public static SortItem desc(String field) {
        return new SortItem(field, SortDirection.DESC);
    }
}