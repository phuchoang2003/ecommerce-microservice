package com.hdp.order_service.infrastructure.adapter.inbound.web.dto.request;

import com.hdp.order_service.domain.valueobject.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
@Schema(description = "Request to create an order")
public record CreateOrderRequest(
        @NotNull(message = "{validation.notNull}")
        @Schema(description = "Buyer ID")
        UUID buyerId,

        @NotNull(message = "{validation.notNull}")
        @Schema(description = "Shipping address ID")
        UUID shippingAddressId,

        @NotNull(message = "{validation.notNull}")
        @Schema(description = "Payment method", example = "VNPAY")
        PaymentMethod paymentMethod,

        @NotEmpty(message = "{validation.notEmpty}")
        @Schema(description = "List of order items")
        List<@Valid CreateOrderItemRequest> items,

        @Schema(description = "Coupon codes (optional)")
        List<@NotBlank(message = "{validation.notBlank}") String> couponCodes
) {
}
