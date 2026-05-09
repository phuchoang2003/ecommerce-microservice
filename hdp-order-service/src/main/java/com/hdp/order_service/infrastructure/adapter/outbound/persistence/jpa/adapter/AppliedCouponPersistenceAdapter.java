package com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.adapter;

import com.hdp.order_service.application.port.out.AppliedCouponPersistencePort;
import com.hdp.order_service.domain.model.AppliedCoupon;
import com.hdp.order_service.infrastructure.adapter.outbound.persistence.mapper.OrderMapper;
import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.repository.AppliedCouponRepositoryJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AppliedCouponPersistenceAdapter implements AppliedCouponPersistencePort {

    private final AppliedCouponRepositoryJpa repository;
    private final OrderMapper orderMapper;

    @Override
    public List<AppliedCoupon> findByOrderId(UUID orderId) {
        return repository.findByOrderId(orderId).stream()
                .map(orderMapper::toAppliedCouponDomain)
                .toList();
    }
}