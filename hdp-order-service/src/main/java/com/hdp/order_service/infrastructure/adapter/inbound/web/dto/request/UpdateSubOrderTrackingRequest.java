package com.hdp.order_service.infrastructure.adapter.inbound.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "Request to update sub-order tracking")
public record UpdateSubOrderTrackingRequest(
    @Schema(description = "Tracking number", example = "VN123456789")
    String trackingNumber,

    @Schema(description = "Carrier name", example = "GHTK")
    String carrier,

    @Schema(description = "Estimated delivery date")
    LocalDate estimatedDelivery,

    @Schema(description = "Additional notes (optional)")
    String note
) {}