package com.hdp.product_service.domain.model;

import com.hdp.core.model.AggregateRoot;
import com.hdp.product_service.domain.model.valueobject.ProductStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Product extends AggregateRoot<UUID> {

    private final UUID sellerId;
    private final UUID categoryId;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private List<String> images;
    private BigDecimal rating;
    private int soldCount;
    private ProductStatus status;
    private final List<ProductVariant> variants;
    private final Instant createdAt;
    private Instant updatedAt;

    @Builder
    public Product(UUID id, UUID sellerId, UUID categoryId, String name, String description,
                   BigDecimal price, BigDecimal originalPrice, List<String> images,
                   BigDecimal rating, int soldCount, ProductStatus status,
                   List<ProductVariant> variants, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.sellerId = sellerId;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.originalPrice = originalPrice;
        this.images = images != null ? new ArrayList<>(images) : new ArrayList<>();
        this.rating = rating != null ? rating : BigDecimal.ZERO;
        this.soldCount = soldCount;
        this.status = status != null ? status : ProductStatus.DRAFT;
        this.variants = variants != null ? new ArrayList<>(variants) : new ArrayList<>();
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.updatedAt = updatedAt != null ? updatedAt : Instant.now();
    }

    public void update(String name, String description, BigDecimal price, BigDecimal originalPrice,
                       List<String> images, ProductStatus status) {
        if (name != null) this.name = name;
        if (description != null) this.description = description;
        if (price != null) this.price = price;
        if (originalPrice != null) this.originalPrice = originalPrice;
        if (images != null) this.images = new ArrayList<>(images);
        if (status != null) {
            if (!this.status.canTransitionTo(status)) {
                throw new IllegalStateException(
                    "Cannot transition from " + this.status + " to " + status);
            }
            this.status = status;
        }
        this.updatedAt = Instant.now();
    }

    public void updateStatus(ProductStatus newStatus) {
        if (!this.status.canTransitionTo(newStatus)) {
            throw new IllegalStateException(
                "Cannot transition from " + this.status + " to " + newStatus);
        }
        this.status = newStatus;
        this.updatedAt = Instant.now();
    }

    public void addVariant(ProductVariant variant) {
        this.variants.add(variant);
    }

    public void clearVariants() {
        this.variants.clear();
    }
}