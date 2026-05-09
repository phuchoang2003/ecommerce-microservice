package com.hdp.order_service.infrastructure.adapter.inbound.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
@Schema(description = "Request to cancel an order")
public record CancelOrderRequest(
    @NotNull(message = "{validation.notNull}")
    @Schema(description = "ID of user cancelling the order")
    UUID cancelledBy,

    @Schema(description = "Reason for cancellation (optional)")
    String reason
) {}
