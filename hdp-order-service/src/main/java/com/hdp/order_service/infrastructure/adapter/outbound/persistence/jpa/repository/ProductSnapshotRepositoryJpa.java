package com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.repository;

import com.hdp.common.persistence.repository.BaseRepositoryJpa;
import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.entity.ProductSnapshotJpa;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductSnapshotRepositoryJpa extends BaseRepositoryJpa<ProductSnapshotJpa, UUID> {

    Optional<ProductSnapshotJpa> findByProductIdAndVariantId(UUID productId, UUID variantId);

    List<ProductSnapshotJpa> findByProductIdIn(List<UUID> productIds);

    boolean existsByProductId(UUID productId);
}