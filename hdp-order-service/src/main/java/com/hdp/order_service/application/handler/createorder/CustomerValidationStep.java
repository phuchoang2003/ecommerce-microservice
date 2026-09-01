package com.hdp.order_service.application.handler.createorder;

import com.hdp.order_service.application.port.in.createorder.CreateOrderCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class CustomerValidationStep extends AbstractOrderValidationStep {
    @Override
    protected void doValidate(CreateOrderCommand command) {
        log.info("Customer validation step");
    }
}
