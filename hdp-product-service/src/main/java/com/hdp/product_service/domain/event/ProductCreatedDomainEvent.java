package com.hdp.product_service.domain.event;

import com.hdp.core.event.DomainEvent;
import com.hdp.product_service.domain.model.valueobject.ProductStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
public class ProductCreatedDomainEvent extends DomainEvent {

    private final UUID productId;
    private final UUID sellerId;
    private final UUID categoryId;
    private final String name;
    private final BigDecimal price;
    private final List<String> images;
    private final ProductStatus status;

    @Builder
    public ProductCreatedDomainEvent(UUID productId, UUID sellerId, UUID categoryId,
                                      String name, BigDecimal price, List<String> images,
                                      ProductStatus status) {
        this.productId = productId;
        this.sellerId = sellerId;
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.images = images;
        this.status = status;
    }

    @Override
    public String getAggregateId() {
        return productId.toString();
    }
}