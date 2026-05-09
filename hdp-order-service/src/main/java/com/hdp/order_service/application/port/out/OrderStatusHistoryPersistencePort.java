package com.hdp.order_service.application.port.out;

import com.hdp.order_service.domain.model.OrderStatusHistory;

import java.util.List;
import java.util.UUID;

public interface OrderStatusHistoryPersistencePort {
    void save(OrderStatusHistory history);
    List<OrderStatusHistory> findByOrderIdOrderByCreatedAtDesc(UUID orderId);
}