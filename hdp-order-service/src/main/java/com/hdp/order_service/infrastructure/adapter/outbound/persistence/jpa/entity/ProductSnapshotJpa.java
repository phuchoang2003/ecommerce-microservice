package com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.entity;

import com.hdp.common.persistence.entity.BaseEntityJpa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@Entity
@Table(name = "products_snapshot")
@AllArgsConstructor
@RequiredArgsConstructor
public class ProductSnapshotJpa extends BaseEntityJpa {

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "variant_id", nullable = false)
    private UUID variantId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "variant_name", length = 500)
    private String variantName;

    @Column(name = "price", nullable = false, precision = 19, scale = 4)
    private BigDecimal price;
}