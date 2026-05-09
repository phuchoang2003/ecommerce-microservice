package com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.repository;

import com.hdp.common.persistence.repository.BaseRepositoryJpa;
import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.entity.SubOrderJpa;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubOrderRepositoryJpa extends BaseRepositoryJpa<SubOrderJpa, UUID> {

    List<SubOrderJpa> findByOrderId(UUID orderId);

    List<SubOrderJpa> findBySellerId(UUID sellerId);

    Optional<SubOrderJpa> findByIdAndNotDeleted(UUID id);
}
