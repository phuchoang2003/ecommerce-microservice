package com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.repository;

import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.entity.ProductSnapshotJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductSnapshotRepositoryJpa extends JpaRepository<ProductSnapshotJpa, UUID> {

    List<ProductSnapshotJpa> findByProductIdIn(List<UUID> productIds);

    boolean existsByProductId(UUID productId);
}