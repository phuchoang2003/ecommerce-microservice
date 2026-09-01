package com.hdp.order_service.application.port.in.getorderhistory;

import java.util.UUID;

public record GetOrderHistoryQuery(UUID orderId) {
}
