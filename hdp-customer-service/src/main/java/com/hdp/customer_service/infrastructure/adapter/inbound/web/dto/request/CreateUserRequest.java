package com.hdp.customer_service.infrastructure.adapter.inbound.web.dto.request;

import com.hdp.customer_service.domain.valueobject.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Request to create a new user")
public record CreateUserRequest(
    @NotBlank(message = "{validation.notBlank}")
    @Size(max = 255, message = "{validation.size}")
    @Schema(description = "Full name", example = "Nguyen Van A")
    String fullName,

    @NotBlank(message = "{validation.notBlank}")
    @Email(message = "{validation.email}")
    @Size(max = 255, message = "{validation.size}")
    @Schema(description = "Email (must be unique)", example = "user@example.com")
    String email,

    @Size(max = 20, message = "{validation.size}")
    @Schema(description = "Phone (optional)", example = "+84123456789")
    String phone,

    @Past(message = "{validation.past}")
    @Schema(description = "Date of birth (optional)")
    LocalDate dateOfBirth,

    @Schema(description = "Gender (optional)", example = "MALE")
    Gender gender,

    @Size(max = 500, message = "{validation.size}")
    @Schema(description = "Avatar URL (optional)")
    String avatarUrl,

    @Schema(description = "List of addresses (optional)")
    List<@Valid AddressRequest> addresses
) {
}
