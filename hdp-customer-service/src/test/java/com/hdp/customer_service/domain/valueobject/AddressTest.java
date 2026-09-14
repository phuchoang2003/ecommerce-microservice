package com.hdp.customer_service.domain.valueobject;

import com.hdp.core.exception.BusinessException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AddressTest {

    @Test
    void create_succeedsWithAllRequiredFields() {
        assertThatCode(() -> new Address(null, "123 Le Loi", "Ward 1", "District 1", "HCMC", "VN"))
            .doesNotThrowAnyException();
    }

    @Test
    void create_succeedsWithoutOptionalFields() {
        assertThatCode(() -> new Address(null, "123 Le Loi", null, null, "HCMC", "VN"))
            .doesNotThrowAnyException();
    }

    @Test
    void create_throwsOnBlankStreet() {
        assertThatThrownBy(() -> new Address(null, "  ", null, null, "HCMC", "VN"))
            .isInstanceOf(BusinessException.class)
            .satisfies(t -> {
                BusinessException ex = (BusinessException) t;
                assertThat(ex.getMessage()).isEqualTo("ADDRESS_FIELD_REQUIRED");
                assertThat(ex.getMessageArgs()[0]).isEqualTo("street");
            });
    }

    @Test
    void create_throwsOnBlankCity() {
        assertThatThrownBy(() -> new Address(null, "123 Le Loi", null, null, " ", "VN"))
            .isInstanceOf(BusinessException.class)
            .satisfies(t -> {
                BusinessException ex = (BusinessException) t;
                assertThat(ex.getMessage()).isEqualTo("ADDRESS_FIELD_REQUIRED");
                assertThat(ex.getMessageArgs()[0]).isEqualTo("city");
            });
    }

    @Test
    void create_throwsOnBlankCountry() {
        assertThatThrownBy(() -> new Address(null, "123 Le Loi", null, null, "HCMC", ""))
            .isInstanceOf(BusinessException.class)
            .satisfies(t -> {
                BusinessException ex = (BusinessException) t;
                assertThat(ex.getMessage()).isEqualTo("ADDRESS_FIELD_REQUIRED");
                assertThat(ex.getMessageArgs()[0]).isEqualTo("country");
            });
    }

    @Test
    void create_throwsOnStreetTooLong() {
        String longStreet = "x".repeat(501);
        assertThatThrownBy(() -> new Address(null, longStreet, null, null, "HCMC", "VN"))
            .isInstanceOf(BusinessException.class)
            .satisfies(t -> {
                BusinessException ex = (BusinessException) t;
                assertThat(ex.getMessage()).isEqualTo("ADDRESS_FIELD_TOO_LONG");
                assertThat(ex.getMessageArgs()[0]).isEqualTo("street");
            });
    }
}
