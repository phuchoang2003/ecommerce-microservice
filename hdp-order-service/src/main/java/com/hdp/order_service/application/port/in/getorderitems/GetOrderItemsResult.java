package com.hdp.order_service.application.port.in.getorderitems;

import java.util.List;

public record GetOrderItemsResult(List<OrderItemView> items) {
}
