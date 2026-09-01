package com.hdp.order_service.application.port.in.listsubordersbyorder;

import java.util.List;

public record ListSubOrdersByOrderResult(List<SubOrderSummary> subOrderResults) {
}
