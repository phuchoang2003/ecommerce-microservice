package com.hdp.order_service.domain.valueobject;

import com.hdp.order_service.domain.exception.OrderNumberFormatException;

import java.util.regex.Pattern;

public record OrderNumber(String value) {
    private static final Pattern PATTERN = Pattern.compile("^[0-9A-Z]{14}$");

    public OrderNumber {
        if (value == null || !PATTERN.matcher(value).matches()) {
            throw new OrderNumberFormatException(value);
        }
    }

    public static OrderNumber of(String value){
        return new OrderNumber(value);
    }
}
