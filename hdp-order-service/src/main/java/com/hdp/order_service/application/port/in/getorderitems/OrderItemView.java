package com.hdp.order_service.application.port.in.getorderitems;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemView(
    UUID id, UUID orderId, UUID subOrderId, UUID sellerId,
    UUID productId, UUID variantId, String productName, String variantName,
    BigDecimal price, Integer quantity, BigDecimal subtotal
) {
}
