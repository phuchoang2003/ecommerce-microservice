package com.hdp.order_service.application.handler.createorder;

import com.hdp.core.exception.NotFoundException;
import com.hdp.order_service.application.port.in.createorder.CreateOrderCommand;
import com.hdp.order_service.application.port.out.CustomerClientPort;
import com.hdp.order_service.domain.valueobject.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderValidationChainTest {

    @Mock
    private CustomerClientPort customerClientPort;

    private CustomerValidationStep customerStep;
    private FraudValidationStep fraudStep;
    private OrderValidationStep chain;

    @BeforeEach
    void setUp() {
        customerStep = new CustomerValidationStep(customerClientPort);
        fraudStep = spy(new FraudValidationStep());
        chain = customerStep.next(fraudStep);
    }

    @Test
    void validate_runsCustomerStepFirstThenFraudStep() {
        UUID buyerId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();
        CreateOrderCommand cmd = new CreateOrderCommand(
            buyerId, addressId, PaymentMethod.COD, List.of(), List.of()
        );
        when(customerClientPort.findAddressIdsByBuyerId(buyerId)).thenReturn(List.of(addressId));

        chain.validate(cmd);

        verify(customerClientPort).findAddressIdsByBuyerId(buyerId);
        verify(fraudStep).doValidate(cmd);
    }

    @Test
    void validate_stopsChainWhenCustomerStepThrows() {
        UUID buyerId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();
        CreateOrderCommand cmd = new CreateOrderCommand(
            buyerId, addressId, PaymentMethod.COD, List.of(), List.of()
        );
        doThrow(new NotFoundException("User", buyerId))
            .when(customerClientPort).findAddressIdsByBuyerId(any());

        assertThatThrownBy(() -> chain.validate(cmd))
            .isInstanceOf(NotFoundException.class);

        verify(fraudStep, never()).doValidate(any());
    }
}
