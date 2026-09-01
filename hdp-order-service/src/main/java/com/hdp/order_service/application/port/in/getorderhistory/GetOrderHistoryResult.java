package com.hdp.order_service.application.port.in.getorderhistory;

import java.util.List;

public record GetOrderHistoryResult(List<OrderStatusHistoryView> histories) {
}
