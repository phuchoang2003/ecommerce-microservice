package com.hdp.order_service.application.port.out;

import com.hdp.order_service.domain.model.OrderItem;

import java.util.List;
import java.util.UUID;

public interface OrderItemPersistencePort {
    List<OrderItem> findByOrderId(UUID orderId);
    List<OrderItem> findBySubOrderId(UUID subOrderId);
}