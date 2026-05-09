package com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.repository;

import com.hdp.common.persistence.repository.BaseRepositoryJpa;
import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.entity.OrderStatusHistoryJpa;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderStatusHistoryRepositoryJpa extends BaseRepositoryJpa<OrderStatusHistoryJpa, UUID> {

    List<OrderStatusHistoryJpa> findByOrderIdOrderByCreatedAtDesc(UUID orderId);
}
