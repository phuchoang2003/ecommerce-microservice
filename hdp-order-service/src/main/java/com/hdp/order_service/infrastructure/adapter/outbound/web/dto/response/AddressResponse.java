package com.hdp.order_service.infrastructure.adapter.outbound.web.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AddressResponse(
    UUID id,
    String street,
    String ward,
    String district,
    String city,
    String country
) {
}
