package com.hdp.customer_service.infrastructure.adapter.inbound.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Address")
public record AddressRequest(
    @NotBlank(message = "{validation.notBlank}")
    @Size(max = 500, message = "{validation.size}")
    @Schema(description = "Street address", example = "123 Le Loi")
    String street,

    @Size(max = 100, message = "{validation.size}")
    @Schema(description = "Ward (optional)")
    String ward,

    @Size(max = 100, message = "{validation.size}")
    @Schema(description = "District (optional)")
    String district,

    @NotBlank(message = "{validation.notBlank}")
    @Size(max = 100, message = "{validation.size}")
    @Schema(description = "City", example = "Ho Chi Minh")
    String city,

    @NotBlank(message = "{validation.notBlank}")
    @Size(max = 100, message = "{validation.size}")
    @Schema(description = "Country", example = "VN")
    String country
) {
}
