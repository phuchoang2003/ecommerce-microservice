package com.hdp.order_service.domain.exception;

import com.hdp.core.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderErrorCode implements ErrorCode {
    ORDER_NUMBER_INVALID(
            "ORDER_NUMBER_INVALID",
            "order.number.invalid"
    ),
    ORDER_INVALID_STATUS_TRANSITION(
            "ORDER_INVALID_STATUS_TRANSITION",
            "order.status_transition.invalid"
    ),
    ORDER_NOT_CANCELLABLE(
            "ORDER_NOT_CANCELLABLE",
            "order.cancellation.not_allowed"
    ),
    SUBORDER_INVALID_STATUS_TRANSITION(
            "SUBORDER_INVALID_STATUS_TRANSITION",
            "suborder.status_transition.invalid"
    ),
    SUBORDER_NOT_CANCELLABLE(
            "SUBORDER_NOT_CANCELLABLE",
            "suborder.cancellation.not_allowed"
    ),
    SUBORDER_INVALID_TRACKING_UPDATE(
            "SUBORDER_INVALID_TRACKING_UPDATE",
            "suborder.tracking.update.invalid"
    );

    private final String code;
    private final String messageKey;
}