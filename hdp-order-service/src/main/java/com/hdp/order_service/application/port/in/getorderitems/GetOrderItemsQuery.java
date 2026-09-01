package com.hdp.order_service.application.port.in.getorderitems;

import java.util.UUID;

public record GetOrderItemsQuery(UUID orderId) {
}
