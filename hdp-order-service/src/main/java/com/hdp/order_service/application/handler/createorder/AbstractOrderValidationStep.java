package com.hdp.order_service.application.handler.createorder;


import com.hdp.order_service.application.port.in.createorder.CreateOrderCommand;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractOrderValidationStep implements OrderValidationStep {
    private OrderValidationStep next;

    public final OrderValidationStep next(OrderValidationStep next) {
        this.next = next;
        return next;
    }

    @Override
    public final void validate(CreateOrderCommand command) {
        doValidate(command);
        if(next != null){
            log.debug("Next step: {}", next.getClass().getSimpleName());
            next.validate(command);
        }
    }

    protected abstract void doValidate(CreateOrderCommand command);
}
