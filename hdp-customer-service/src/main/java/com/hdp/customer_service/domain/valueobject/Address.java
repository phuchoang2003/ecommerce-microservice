package com.hdp.customer_service.domain.valueobject;

import com.hdp.core.exception.BusinessException;

import java.util.UUID;

public record Address(
        UUID id,
        String street,
        String ward,
        String district,
        String city,
        String country
) {
    private static final int STREET_MAX = 500;
    private static final int LOCALITY_MAX = 100;
    private static final int COUNTRY_MAX = 100;

    public Address {
        requireText("street", street, STREET_MAX);
        requireOptionalText("ward", ward, LOCALITY_MAX);
        requireOptionalText("district", district, LOCALITY_MAX);
        requireText("city", city, LOCALITY_MAX);
        requireText("country", country, COUNTRY_MAX);
    }

    private static void requireText(String field, String value, int max) {
        if (value == null || value.isBlank()) {
            throw new BusinessException("ADDRESS_FIELD_REQUIRED", field);
        }
        if (value.length() > max) {
            throw new BusinessException("ADDRESS_FIELD_TOO_LONG", field, max);
        }
    }

    private static void requireOptionalText(String field, String value, int max) {
        if (value == null) {
            return;
        }
        if (value.isBlank()) {
            throw new BusinessException("ADDRESS_FIELD_BLANK", field);
        }
        if (value.length() > max) {
            throw new BusinessException("ADDRESS_FIELD_TOO_LONG", field, max);
        }
    }
}
