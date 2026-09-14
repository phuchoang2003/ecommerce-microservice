package com.hdp.order_service.infrastructure.adapter.outbound.web.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserResponse(
    UUID id,
    List<AddressResponse> addresses
) {
}
