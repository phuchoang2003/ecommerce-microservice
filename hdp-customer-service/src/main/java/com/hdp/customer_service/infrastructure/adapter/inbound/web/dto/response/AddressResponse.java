package com.hdp.customer_service.infrastructure.adapter.inbound.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Builder
@Schema(description = "Address")
public record AddressResponse(
    UUID id,
    String street,
    String ward,
    String district,
    String city,
    String country
) {
}
