package com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.repository;

import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.entity.SubOrderJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubOrderRepositoryJpa extends JpaRepository<SubOrderJpa, UUID> {

    List<SubOrderJpa> findByOrderId(UUID orderId);

    Optional<SubOrderJpa> findByIdAndIsDeletedFalse(UUID id);
}
