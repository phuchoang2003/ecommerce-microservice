package com.hdp.customer_service.infrastructure.adapter.outbound.persistence.mapper;

import com.hdp.customer_service.domain.model.User;
import com.hdp.customer_service.domain.valueobject.Address;
import com.hdp.customer_service.domain.valueobject.Gender;
import com.hdp.customer_service.infrastructure.adapter.outbound.persistence.jpa.entity.UserAddressJpa;
import com.hdp.customer_service.infrastructure.adapter.outbound.persistence.jpa.entity.UserJpa;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private final UserMapper mapper = new UserMapper();

    @Test
    void toDomain_mapsAllFields() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        UserJpa jpa = UserJpa.builder()
            .id(id)
            .fullName("Alice")
            .email("alice@example.com")
            .phone("+84123456789")
            .dateOfBirth(LocalDate.of(1990, 1, 1))
            .gender(Gender.FEMALE)
            .avatarUrl("https://cdn/a.png")
            .deletedAt(null)
            .createdAt(now)
            .updatedAt(now)
            .addresses(new ArrayList<>(List.of(
                UserAddressJpa.builder()
                    .street("123 Le Loi")
                    .city("HCMC")
                    .country("VN")
                    .build()
            )))
            .build();

        User domain = mapper.toDomain(jpa);

        assertThat(domain).isNotNull();
        assertThat(domain.getId().value()).isEqualTo(id);
        assertThat(domain.getFullName()).isEqualTo("Alice");
        assertThat(domain.getEmail()).isEqualTo("alice@example.com");
        assertThat(domain.getPhone()).isEqualTo("+84123456789");
        assertThat(domain.getDateOfBirth()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(domain.getGender()).isEqualTo(Gender.FEMALE);
        assertThat(domain.getAvatarUrl()).isEqualTo("https://cdn/a.png");
        assertThat(domain.getDeletedAt()).isNull();
        assertThat(domain.getCreatedAt()).isEqualTo(now);
        assertThat(domain.getUpdatedAt()).isEqualTo(now);
        assertThat(domain.getAddresses()).hasSize(1);
        assertThat(domain.getAddresses().get(0).city()).isEqualTo("HCMC");
    }

    @Test
    void toDomain_handlesNullInput() {
        assertThat(mapper.toDomain(null)).isNull();
    }

    @Test
    void toDomain_handlesNullAndEmptyAddresses() {
        UserJpa withNull = UserJpa.builder()
            .id(UUID.randomUUID())
            .fullName("X").email("x@y.com")
            .createdAt(Instant.now()).updatedAt(Instant.now())
            .addresses(null)
            .build();
        assertThat(mapper.toDomain(withNull).getAddresses()).isEmpty();

        UserJpa withEmpty = UserJpa.builder()
            .id(UUID.randomUUID())
            .fullName("X").email("x@y.com")
            .createdAt(Instant.now()).updatedAt(Instant.now())
            .addresses(new ArrayList<>())
            .build();
        assertThat(mapper.toDomain(withEmpty).getAddresses()).isEmpty();
    }

    @Test
    void applyToJpa_replacesAddressesEntirely() {
        UserJpa jpa = UserJpa.builder()
            .id(UUID.randomUUID())
            .fullName("Old").email("old@y.com")
            .createdAt(Instant.now()).updatedAt(Instant.now())
            .addresses(new ArrayList<>(List.of(
                UserAddressJpa.builder().street("OLD1").city("OLD_CITY").country("VN").build(),
                UserAddressJpa.builder().street("OLD2").city("OLD_CITY").country("VN").build()
            )))
            .build();

        User update = User.reconstitute(
            com.hdp.customer_service.domain.valueobject.UserId.of(jpa.getId()),
            "New Name", "new@y.com", null, null, null, null,
            List.of(new Address(null, "NEW1", null, null, "NEW_CITY", "VN")),
            null, Instant.now(), Instant.now()
        );

        mapper.applyToJpa(update, jpa);

        assertThat(jpa.getFullName()).isEqualTo("New Name");
        assertThat(jpa.getEmail()).isEqualTo("new@y.com");
        assertThat(jpa.getAddresses()).hasSize(1);
        assertThat(jpa.getAddresses().get(0).getStreet()).isEqualTo("NEW1");
        assertThat(jpa.getAddresses().get(0).getCity()).isEqualTo("NEW_CITY");
        assertThat(jpa.getAddresses().get(0).getUser()).isSameAs(jpa);
    }

    @Test
    void applyToJpa_clearsAddressesWhenEmpty() {
        UserJpa jpa = UserJpa.builder()
            .id(UUID.randomUUID())
            .fullName("X").email("x@y.com")
            .createdAt(Instant.now()).updatedAt(Instant.now())
            .addresses(new ArrayList<>(List.of(
                UserAddressJpa.builder().street("A").city("C").country("VN").build()
            )))
            .build();

        User update = User.reconstitute(
            com.hdp.customer_service.domain.valueobject.UserId.of(jpa.getId()),
            "X", "x@y.com", null, null, null, null,
            List.of(),
            null, Instant.now(), Instant.now()
        );

        mapper.applyToJpa(update, jpa);

        assertThat(jpa.getAddresses()).isEmpty();
    }
}
