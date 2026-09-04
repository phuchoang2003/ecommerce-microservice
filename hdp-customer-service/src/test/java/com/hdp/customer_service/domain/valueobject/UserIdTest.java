package com.hdp.customer_service.domain.valueobject;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserIdTest {

    @Test
    void generate_producesNonNullUuid() {
        UserId id = UserId.generate();
        assertThat(id).isNotNull();
        assertThat(id.value()).isNotNull();
    }

    @Test
    void generate_eachCallProducesDifferentId() {
        UserId first = UserId.generate();
        UserId second = UserId.generate();
        assertThat(first).isNotEqualTo(second);
    }

    @Test
    void of_wrapsGivenUuid() {
        UUID raw = UUID.randomUUID();
        UserId id = UserId.of(raw);
        assertThat(id.value()).isEqualTo(raw);
    }

    @Test
    void of_throwsOnNull() {
        assertThatThrownBy(() -> UserId.of(null))
            .isInstanceOf(NullPointerException.class);
    }
}
