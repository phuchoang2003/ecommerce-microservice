package com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.adapter;

import com.hdp.order_service.application.port.out.OrderStatusHistoryPersistencePort;
import com.hdp.order_service.domain.model.OrderStatusHistory;
import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.entity.OrderJpa;
import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.entity.OrderStatusHistoryJpa;
import com.hdp.order_service.infrastructure.adapter.outbound.persistence.mapper.OrderMapper;
import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.repository.OrderStatusHistoryRepositoryJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderStatusHistoryPersistenceAdapter implements OrderStatusHistoryPersistencePort {

    private final OrderStatusHistoryRepositoryJpa repository;
    private final OrderMapper orderMapper;

    @Override
    public void save(OrderStatusHistory history) {
        OrderStatusHistoryJpa jpa = orderMapper.toStatusHistoryJpa(history);
        repository.save(jpa);
    }

    @Override
    public List<OrderStatusHistory> findByOrderIdOrderByCreatedAtDesc(UUID orderId) {
        return repository.findByOrderIdOrderByCreatedAtDesc(orderId).stream()
                .map(orderMapper::toStatusHistoryDomain)
                .toList();
    }
}