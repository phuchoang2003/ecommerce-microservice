package com.hdp.order_service.application.port.in.listsubordersbyorder;

import java.util.UUID;

public record ListSubOrdersByOrderQuery(UUID orderId) {
}
