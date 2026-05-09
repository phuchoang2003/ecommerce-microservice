package com.hdp.order_service.domain.model.valueobject;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record ProductSnapshot(
    UUID productId,
    UUID variantId,
    String productName,
    String variantName,
    BigDecimal price
) {}