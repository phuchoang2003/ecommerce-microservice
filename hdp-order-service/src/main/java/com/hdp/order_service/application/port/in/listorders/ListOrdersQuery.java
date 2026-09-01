package com.hdp.order_service.application.port.in.listorders;

import com.hdp.order_service.domain.model.valueobject.OrderStatus;

import java.util.UUID;

public record ListOrdersQuery(UUID buyerId, OrderStatus status, int page, int size) {
}
