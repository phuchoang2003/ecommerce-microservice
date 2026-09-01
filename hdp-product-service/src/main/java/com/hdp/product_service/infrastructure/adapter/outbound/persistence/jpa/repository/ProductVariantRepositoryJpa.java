package com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.repository;

import com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.entity.ProductVariantJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductVariantRepositoryJpa extends JpaRepository<ProductVariantJpa, UUID> {

    List<ProductVariantJpa> findByProductIdAndIsDeletedFalse(UUID productId);

    void deleteByProductId(UUID productId);
}