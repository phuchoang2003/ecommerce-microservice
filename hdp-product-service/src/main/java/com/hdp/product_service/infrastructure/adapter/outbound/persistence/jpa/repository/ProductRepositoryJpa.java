package com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.repository;

import java.util.Optional;
import java.util.UUID;

import com.hdp.common.persistence.repository.BaseRepositoryJpa;
import com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.entity.ProductJpa;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepositoryJpa extends BaseRepositoryJpa<ProductJpa, UUID>,
        JpaSpecificationExecutor<ProductJpa> {

    Optional<ProductJpa> findByIdAndIsDeletedFalse(UUID id);
}