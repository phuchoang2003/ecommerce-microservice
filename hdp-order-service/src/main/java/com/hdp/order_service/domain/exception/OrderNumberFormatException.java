package com.hdp.order_service.domain.exception;

import com.hdp.core.exception.BusinessException;

public class OrderNumberFormatException extends BusinessException {
    public OrderNumberFormatException(String value) {
        super("Invalid order number format: " + value);
    }}
