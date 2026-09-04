package com.hdp.customer_service.infrastructure.adapter.inbound.web.dto.response;

import com.hdp.customer_service.domain.valueobject.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder
@Schema(description = "User")
public record UserResponse(
    UUID id,
    String fullName,
    String email,
    String phone,
    LocalDate dateOfBirth,
    Gender gender,
    String avatarUrl,
    List<AddressResponse> addresses,
    Instant createdAt,
    Instant updatedAt
) {
}
