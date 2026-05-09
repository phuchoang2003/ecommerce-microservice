package com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.repository;

import com.hdp.common.persistence.repository.BaseRepositoryJpa;
import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.entity.OrderJpa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepositoryJpa extends BaseRepositoryJpa<OrderJpa, UUID> {

    Optional<OrderJpa> findByIdAndNotDeleted(UUID id);

    Optional<OrderJpa> findByOrderNumber(String orderNumber);

    Page<OrderJpa> findAll(Specification<OrderJpa> spec, Pageable pageable);
}
