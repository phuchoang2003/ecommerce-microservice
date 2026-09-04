package com.hdp.order_service.application.handler.createorder;

import com.hdp.core.exception.NotFoundException;
import com.hdp.order_service.application.port.in.createorder.CreateOrderCommand;
import com.hdp.order_service.application.port.out.CustomerClientPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;


@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerValidationStep extends AbstractOrderValidationStep {
    private final CustomerClientPort customerClientPort;

    @Override
    protected void doValidate(CreateOrderCommand command) {
        List<UUID> addressIds = customerClientPort.findAddressIdsByBuyerId(command.buyerId());
        if (!addressIds.contains(command.shippingAddressId())) {
            log.warn("Address ownership check failed: buyerId={}, shippingAddressId={}",
                command.buyerId(), command.shippingAddressId());
            throw new NotFoundException("ShippingAddress", command.shippingAddressId());
        }
        log.info("Customer validated: buyerId={}, shippingAddressId={}",
            command.buyerId(), command.shippingAddressId());
    }
}
