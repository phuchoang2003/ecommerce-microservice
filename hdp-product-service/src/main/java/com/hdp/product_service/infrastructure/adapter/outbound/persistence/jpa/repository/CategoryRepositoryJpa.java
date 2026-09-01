package com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.repository;

import com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.entity.CategoryJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepositoryJpa extends JpaRepository<CategoryJpa, UUID> {

    Optional<CategoryJpa> findByIdAndIsDeletedFalse(UUID id);

    List<CategoryJpa> findAllByIsDeletedFalse();

    List<CategoryJpa> findByParentIdAndIsDeletedFalse(UUID parentId);

    List<CategoryJpa> findByParentIdIsNullAndIsDeletedFalse();
}