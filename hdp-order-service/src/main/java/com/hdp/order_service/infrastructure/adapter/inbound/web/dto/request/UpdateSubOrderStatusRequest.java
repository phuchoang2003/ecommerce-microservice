package com.hdp.order_service.infrastructure.adapter.inbound.web.dto.request;

import com.hdp.order_service.domain.valueobject.SubOrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
@Schema(description = "Request to update sub-order status")
public record UpdateSubOrderStatusRequest(
    @NotNull(message = "{validation.notNull}")
    @Schema(description = "New status", example = "SHIPPED")
    SubOrderStatus status,

    @NotNull(message = "{validation.notNull}")
    @Schema(description = "ID of user making the change")
    UUID changedBy,

    @Schema(description = "Reason for change (optional)")
    String reason
) {}
