package com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.repository;

import com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.entity.ProductJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepositoryJpa extends JpaRepository<ProductJpa, UUID>,
        JpaSpecificationExecutor<ProductJpa> {

    Optional<ProductJpa> findByIdAndIsDeletedFalse(UUID id);
}