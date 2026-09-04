package com.hdp.order_service.domain.exception;

import com.hdp.core.exception.BusinessException;

public class OrderNumberFormatException extends BusinessException {
    public OrderNumberFormatException(String value) {
        super(OrderErrorCode.ORDER_NUMBER_INVALID, value);
    }
}