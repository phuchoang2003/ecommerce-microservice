package com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.repository;

import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.entity.OrderItemJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderItemRepositoryJpa extends JpaRepository<OrderItemJpa, UUID> {

    List<OrderItemJpa> findByOrderId(UUID orderId);

    List<OrderItemJpa> findBySubOrderId(UUID subOrderId);
}
