package com.hdp.order_service.application.port.in.getsuborderitems;

import java.math.BigDecimal;
import java.util.UUID;

public record SubOrderItemView(
    UUID id, UUID orderId, UUID subOrderId, UUID sellerId,
    UUID productId, UUID variantId, String productName, String variantName,
    BigDecimal price, Integer quantity, BigDecimal subtotal
) {
}
