package com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.repository;

import com.hdp.common.persistence.repository.BaseRepositoryJpa;
import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.entity.AppliedCouponJpa;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AppliedCouponRepositoryJpa extends BaseRepositoryJpa<AppliedCouponJpa, UUID> {

    List<AppliedCouponJpa> findByOrderId(UUID orderId);
}
