package com.hdp.order_service.domain.exception;

public enum OrderErrorCode {
    ORDER_NUMBER_INVALID(
            "ORDER_NUMBER_INVALID",
            "order.number.invalid"
    );



    private final String code;
    private final String messageKey;

    OrderErrorCode(
            String code,
            String messageKey
    ) {
        this.code = code;
        this.messageKey = messageKey;
    }

}
