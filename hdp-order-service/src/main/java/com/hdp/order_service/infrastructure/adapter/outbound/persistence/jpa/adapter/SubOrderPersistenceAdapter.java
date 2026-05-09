package com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.adapter;

import com.hdp.order_service.application.port.out.SubOrderPersistencePort;
import com.hdp.order_service.domain.model.SubOrder;
import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.entity.SubOrderJpa;
import com.hdp.order_service.infrastructure.adapter.outbound.persistence.mapper.OrderMapper;
import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.repository.SubOrderRepositoryJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SubOrderPersistenceAdapter implements SubOrderPersistencePort {

    private final SubOrderRepositoryJpa repository;
    private final OrderMapper orderMapper;

    @Override
    public SubOrder save(SubOrder subOrder) {
        SubOrderJpa jpa = orderMapper.toSubOrderJpa(subOrder);
        jpa = repository.save(jpa);
        return orderMapper.toSubOrderDomain(jpa);
    }

    @Override
    public Optional<SubOrder> findById(UUID id) {
        return repository.findById(id)
                .map(orderMapper::toSubOrderDomain);
    }

    @Override
    public Optional<SubOrder> findByIdAndNotDeleted(UUID id) {
        return repository.findByIdAndNotDeleted(id)
                .map(orderMapper::toSubOrderDomain);
    }

    @Override
    public List<SubOrder> findByOrderId(UUID orderId) {
        return repository.findByOrderId(orderId).stream()
                .map(orderMapper::toSubOrderDomain)
                .toList();
    }

    @Override
    public SubOrder getById(UUID id) {
        return repository.findById(id)
                .map(orderMapper::toSubOrderDomain)
                .orElse(null);
    }
}