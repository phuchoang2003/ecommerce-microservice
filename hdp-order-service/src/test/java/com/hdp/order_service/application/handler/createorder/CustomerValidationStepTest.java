package com.hdp.order_service.application.handler.createorder;

import com.hdp.core.exception.NotFoundException;
import com.hdp.order_service.application.port.in.createorder.CreateOrderCommand;
import com.hdp.order_service.application.port.out.CustomerClientPort;
import com.hdp.order_service.domain.valueobject.PaymentMethod;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerValidationStepTest {

    @Mock
    private CustomerClientPort customerClientPort;

    @InjectMocks
    private CustomerValidationStep step;

    @Test
    void doValidate_passesWhenAddressBelongsToBuyer() {
        UUID buyerId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();
        CreateOrderCommand cmd = new CreateOrderCommand(
            buyerId, addressId, PaymentMethod.COD, List.of(), List.of()
        );
        when(customerClientPort.findAddressIdsByBuyerId(buyerId)).thenReturn(List.of(addressId));

        assertThatCode(() -> step.doValidate(cmd)).doesNotThrowAnyException();

        verify(customerClientPort).findAddressIdsByBuyerId(buyerId);
    }

    @Test
    void doValidate_propagatesNotFoundWhenUserMissing() {
        UUID buyerId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();
        CreateOrderCommand cmd = new CreateOrderCommand(
            buyerId, addressId, PaymentMethod.COD, List.of(), List.of()
        );
        when(customerClientPort.findAddressIdsByBuyerId(buyerId))
            .thenThrow(new NotFoundException("User", buyerId));

        assertThatThrownBy(() -> step.doValidate(cmd))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    void doValidate_throwsNotFoundWhenAddressNotInBuyerAddresses() {
        UUID buyerId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();
        UUID otherAddressId = UUID.randomUUID();
        CreateOrderCommand cmd = new CreateOrderCommand(
            buyerId, addressId, PaymentMethod.COD, List.of(), List.of()
        );
        when(customerClientPort.findAddressIdsByBuyerId(buyerId))
            .thenReturn(List.of(otherAddressId));

        assertThatThrownBy(() -> step.doValidate(cmd))
            .isInstanceOf(NotFoundException.class)
            .satisfies(e -> {
                NotFoundException ex = (NotFoundException) e;
                org.assertj.core.api.Assertions.assertThat(ex.getMessageArgs()[0])
                    .isEqualTo("ShippingAddress");
                org.assertj.core.api.Assertions.assertThat(ex.getMessageArgs()[1])
                    .isEqualTo(addressId);
            });
    }

    @Test
    void doValidate_throwsNotFoundWhenBuyerHasNoAddresses() {
        UUID buyerId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();
        CreateOrderCommand cmd = new CreateOrderCommand(
            buyerId, addressId, PaymentMethod.COD, List.of(), List.of()
        );
        when(customerClientPort.findAddressIdsByBuyerId(buyerId)).thenReturn(List.of());

        assertThatThrownBy(() -> step.doValidate(cmd))
            .isInstanceOf(NotFoundException.class);
    }
}
