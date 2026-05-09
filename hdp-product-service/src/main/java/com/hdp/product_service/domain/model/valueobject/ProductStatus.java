package com.hdp.product_service.domain.model.valueobject;

public enum ProductStatus {
    DRAFT,
    ACTIVE,
    INACTIVE,
    DELETED;

    public boolean canTransitionTo(ProductStatus newStatus) {
        return switch (this) {
            case DRAFT -> newStatus == ACTIVE || newStatus == DELETED;
            case ACTIVE -> newStatus == INACTIVE || newStatus == DELETED;
            case INACTIVE -> newStatus == ACTIVE || newStatus == DELETED;
            case DELETED -> false;
        };
    }
}