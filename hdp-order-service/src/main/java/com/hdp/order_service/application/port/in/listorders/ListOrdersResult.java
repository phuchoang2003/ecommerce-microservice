package com.hdp.order_service.application.port.in.listorders;

import java.util.List;

public record ListOrdersResult(List<OrderSummary> orderResults) {
}
