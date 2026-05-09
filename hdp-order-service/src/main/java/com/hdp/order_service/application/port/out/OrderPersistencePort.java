package com.hdp.order_service.application.port.out;

import com.hdp.order_service.domain.model.Order;
import com.hdp.order_service.domain.model.valueobject.OrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderPersistencePort {
    Order save(Order order);
    Optional<Order> findById(UUID id);
    Optional<Order> findByIdAndNotDeleted(UUID id);
    Optional<Order> findByOrderNumber(String orderNumber);
    List<Order> findAll(UUID buyerId, OrderStatus status, int page, int size);
    Order getById(UUID id);
}