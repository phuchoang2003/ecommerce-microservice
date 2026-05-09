package com.hdp.product_service.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariant {

    private UUID id;
    private UUID productId;
    private String sku;
    private BigDecimal price;
    private int stock;
    private String attributes;
    private Instant createdAt;
    private Instant updatedAt;

    public void update(String sku, BigDecimal price, int stock, String attributes) {
        if (sku != null) this.sku = sku;
        if (price != null) this.price = price;
        this.stock = stock;
        if (attributes != null) this.attributes = attributes;
        this.updatedAt = Instant.now();
    }

    public void reserveStock(int quantity) {
        if (this.stock < quantity) {
            throw new IllegalStateException("Insufficient stock for SKU: " + sku);
        }
        this.stock -= quantity;
    }

    public void releaseStock(int quantity) {
        this.stock += quantity;
    }
}