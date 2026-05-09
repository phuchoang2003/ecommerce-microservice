package com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.adapter;

import com.hdp.order_service.application.port.out.OrderItemPersistencePort;
import com.hdp.order_service.domain.model.OrderItem;
import com.hdp.order_service.infrastructure.adapter.outbound.persistence.mapper.OrderMapper;
import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.repository.OrderItemRepositoryJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderItemPersistenceAdapter implements OrderItemPersistencePort {

    private final OrderItemRepositoryJpa repository;
    private final OrderMapper orderMapper;

    @Override
    public List<OrderItem> findByOrderId(UUID orderId) {
        return repository.findByOrderId(orderId).stream()
                .map(orderMapper::toOrderItemDomain)
                .toList();
    }

    @Override
    public List<OrderItem> findBySubOrderId(UUID subOrderId) {
        return repository.findBySubOrderId(subOrderId).stream()
                .map(orderMapper::toOrderItemDomain)
                .toList();
    }
}