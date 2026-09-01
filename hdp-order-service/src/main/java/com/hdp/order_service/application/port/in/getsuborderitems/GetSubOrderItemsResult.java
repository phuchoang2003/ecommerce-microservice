package com.hdp.order_service.application.port.in.getsuborderitems;

import java.util.List;

public record GetSubOrderItemsResult(List<SubOrderItemView> items) {
}
