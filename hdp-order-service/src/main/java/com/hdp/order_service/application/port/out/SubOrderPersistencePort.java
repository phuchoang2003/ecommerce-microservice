package com.hdp.order_service.application.port.out;

import com.hdp.order_service.domain.model.SubOrder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubOrderPersistencePort {
    SubOrder save(SubOrder subOrder);
    Optional<SubOrder> findById(UUID id);
    Optional<SubOrder> findByIdAndNotDeleted(UUID id);
    List<SubOrder> findByOrderId(UUID orderId);
    SubOrder getById(UUID id);
}