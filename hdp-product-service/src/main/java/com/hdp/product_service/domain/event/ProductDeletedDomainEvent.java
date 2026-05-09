package com.hdp.product_service.domain.event;

import com.hdp.core.event.DomainEvent;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ProductDeletedDomainEvent extends DomainEvent {

    private final UUID productId;
    private final UUID sellerId;

    @Builder
    public ProductDeletedDomainEvent(UUID productId, UUID sellerId) {
        this.productId = productId;
        this.sellerId = sellerId;
    }

    @Override
    public String getAggregateId() {
        return productId.toString();
    }
}