package com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.entity;

import com.hdp.common.persistence.entity.BaseEntityJpa;
import com.hdp.product_service.domain.model.valueobject.ProductStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ProductJpa extends BaseEntityJpa {

    @Column(name = "seller_id", nullable = false)
    private UUID sellerId;

    @Column(name = "category_id", nullable = false)
    private UUID categoryId;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", nullable = false, precision = 19, scale = 4)
    private BigDecimal price;

    @Column(name = "original_price", precision = 19, scale = 4)
    private BigDecimal originalPrice;

    @Column(name = "images", columnDefinition = "jsonb")
    @Convert(converter = JsonListConverter.class)
    @Builder.Default
    private List<String> images = new ArrayList<>();

    @Column(name = "rating", precision = 3, scale = 2)
    @Builder.Default
    private BigDecimal rating = BigDecimal.ZERO;

    @Column(name = "sold_count")
    @Builder.Default
    private Integer soldCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private ProductStatus status = ProductStatus.DRAFT;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ProductVariantJpa> variants = new ArrayList<>();
}