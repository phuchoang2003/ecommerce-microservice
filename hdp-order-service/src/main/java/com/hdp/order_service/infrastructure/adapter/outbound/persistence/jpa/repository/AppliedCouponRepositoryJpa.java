package com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.repository;

import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.entity.AppliedCouponJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AppliedCouponRepositoryJpa extends JpaRepository<AppliedCouponJpa, UUID> {

    List<AppliedCouponJpa> findByOrderId(UUID orderId);
}
