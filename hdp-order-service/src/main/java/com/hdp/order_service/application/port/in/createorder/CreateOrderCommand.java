package com.hdp.order_service.application.port.in.createorder;

import com.hdp.order_service.domain.model.valueobject.PaymentMethod;

import java.util.List;
import java.util.UUID;

public record CreateOrderCommand(
    UUID buyerId,
    UUID shippingAddressId,
    PaymentMethod paymentMethod,
    List<CreateOrderItemCommand> items,
    List<String> couponCodes
) {
    public record CreateOrderItemCommand(
        UUID sellerId,
        UUID productId,
        UUID variantId,
        int quantity
    ) {
    }
}
