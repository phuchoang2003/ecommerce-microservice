package com.hdp.order_service.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class OrderItem {
    private final UUID id;
    private final UUID sellerId;
    private final UUID productId;
    private final UUID variantId;
    private final String productName;
    private final String variantName;
    private final BigDecimal price;
    private final Integer quantity;
    private final BigDecimal subtotal;

    @Builder
    public OrderItem(UUID id, UUID sellerId, UUID productId, UUID variantId,
                     String productName, String variantName, BigDecimal price,
                     Integer quantity, BigDecimal subtotal) {
        this.id = id;
        this.sellerId = sellerId;
        this.productId = productId;
        this.variantId = variantId;
        this.productName = productName;
        this.variantName = variantName;
        this.price = price;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    public static OrderItem create(UUID sellerId, UUID productId, UUID variantId,
                                   String productName, String variantName,
                                   BigDecimal price, Integer quantity) {
        BigDecimal calculatedSubtotal = price.multiply(BigDecimal.valueOf(quantity));
        return OrderItem.builder()
                .sellerId(sellerId)
                .productId(productId)
                .variantId(variantId)
                .productName(productName)
                .variantName(variantName)
                .price(price)
                .quantity(quantity)
                .subtotal(calculatedSubtotal)
                .build();
    }
}