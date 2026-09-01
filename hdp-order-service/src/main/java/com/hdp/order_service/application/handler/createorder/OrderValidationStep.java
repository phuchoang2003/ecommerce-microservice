package com.hdp.order_service.application.handler.createorder;

import com.hdp.order_service.application.port.in.createorder.CreateOrderCommand;

public interface OrderValidationStep {
    void validate(CreateOrderCommand command);
}
