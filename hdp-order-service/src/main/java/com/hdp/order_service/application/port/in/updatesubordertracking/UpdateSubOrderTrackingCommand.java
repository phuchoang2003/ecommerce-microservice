package com.hdp.order_service.application.port.in.updatesubordertracking;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateSubOrderTrackingCommand(
    UUID id, String trackingNumber, String carrier, LocalDate estimatedDelivery, String note
) {
}
