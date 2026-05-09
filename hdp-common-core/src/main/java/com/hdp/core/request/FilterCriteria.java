package com.hdp.core.request;

public record FilterCriteria(
    String field,
    FilterOperator operator,
    Object value
) {
    public FilterCriteria {
        if (field == null || field.isBlank()) {
            throw new IllegalArgumentException("Field cannot be null or blank");
        }
        if (operator == null) {
            throw new IllegalArgumentException("Operator cannot be null");
        }
    }

    public static FilterCriteria of(String field, FilterOperator operator, Object value) {
        return new FilterCriteria(field, operator, value);
    }
}