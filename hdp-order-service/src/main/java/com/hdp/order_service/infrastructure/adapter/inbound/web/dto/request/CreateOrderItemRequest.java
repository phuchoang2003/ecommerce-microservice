package com.hdp.order_service.infrastructure.adapter.inbound.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
@Schema(description = "Order item request")
public record CreateOrderItemRequest(
    @NotNull(message = "{validation.notNull}")
    @Schema(description = "Seller ID")
    UUID sellerId,

    @NotNull(message = "{validation.notNull}")
    @Schema(description = "Product ID")
    UUID productId,

    @Schema(description = "Variant ID (optional)")
    UUID variantId,

    @NotNull(message = "{validation.notNull}")
    @Min(value = 1, message = "{validation.min}")
    @Schema(description = "Quantity", example = "2")
    Integer quantity
) {}
