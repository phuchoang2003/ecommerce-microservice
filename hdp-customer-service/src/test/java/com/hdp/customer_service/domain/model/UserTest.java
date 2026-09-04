package com.hdp.customer_service.domain.model;

import com.hdp.core.exception.BusinessException;
import com.hdp.customer_service.domain.valueobject.Address;
import com.hdp.customer_service.domain.valueobject.Gender;
import com.hdp.customer_service.domain.valueobject.UserId;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    @Test
    void create_assignsGeneratedIdAndTimestamps() {
        User user = User.create("Alice", "alice@example.com", "+84123456789",
            LocalDate.of(1990, 1, 1), Gender.FEMALE, "https://cdn/avatar.png",
            List.of(new Address(null, "123 Le Loi", null, null, "HCMC", "VN")));

        assertThat(user.getId()).isNotNull();
        assertThat(user.getFullName()).isEqualTo("Alice");
        assertThat(user.getEmail()).isEqualTo("alice@example.com");
        assertThat(user.getPhone()).isEqualTo("+84123456789");
        assertThat(user.getDateOfBirth()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(user.getGender()).isEqualTo(Gender.FEMALE);
        assertThat(user.getAvatarUrl()).isEqualTo("https://cdn/avatar.png");
        assertThat(user.getAddresses()).hasSize(1);
        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getUpdatedAt()).isNotNull();
        assertThat(user.getDeletedAt()).isNull();
        assertThat(user.isDeleted()).isFalse();
    }

    @Test
    void create_acceptsEmptyAddressList() {
        User user = User.create("Bob", "bob@example.com", null, null, null, null, List.of());
        assertThat(user.getAddresses()).isEmpty();
    }

    @Test
    void create_throwsOnBlankFullName() {
        assertThatThrownBy(() -> User.create(" ", "x@y.com", null, null, null, null, null))
            .isInstanceOf(BusinessException.class)
            .satisfies(t -> {
                BusinessException ex = (BusinessException) t;
                assertThat(ex.getMessage()).isEqualTo("USER_FIELD_REQUIRED");
                assertThat(ex.getMessageArgs()[0]).isEqualTo("fullName");
            });
    }

    @Test
    void create_throwsOnBlankEmail() {
        assertThatThrownBy(() -> User.create("Alice", "  ", null, null, null, null, null))
            .isInstanceOf(BusinessException.class)
            .satisfies(t -> {
                BusinessException ex = (BusinessException) t;
                assertThat(ex.getMessage()).isEqualTo("USER_FIELD_REQUIRED");
                assertThat(ex.getMessageArgs()[0]).isEqualTo("email");
            });
    }

    @Test
    void create_throwsOnBlankPhone() {
        assertThatThrownBy(() -> User.create("Alice", "x@y.com", " ", null, null, null, null))
            .isInstanceOf(BusinessException.class)
            .satisfies(t -> {
                BusinessException ex = (BusinessException) t;
                assertThat(ex.getMessage()).isEqualTo("USER_FIELD_BLANK");
                assertThat(ex.getMessageArgs()[0]).isEqualTo("phone");
            });
    }

    @Test
    void update_replacesAllFieldsAndBumpsUpdatedAt() throws InterruptedException {
        User user = User.create("Alice", "alice@example.com", "+84123456789",
            LocalDate.of(1990, 1, 1), Gender.FEMALE, "old-avatar",
            List.of(new Address(null, "123 Le Loi", null, null, "HCMC", "VN")));

        Instant originalUpdatedAt = user.getUpdatedAt();
        Thread.sleep(10);

        user.update("Alice Updated", "alice2@example.com", "+84987654321",
            LocalDate.of(1991, 2, 2), Gender.OTHER, "new-avatar",
            List.of(new Address(null, "456 Tran Hung Dao", null, null, "Hanoi", "VN")));

        assertThat(user.getFullName()).isEqualTo("Alice Updated");
        assertThat(user.getEmail()).isEqualTo("alice2@example.com");
        assertThat(user.getPhone()).isEqualTo("+84987654321");
        assertThat(user.getDateOfBirth()).isEqualTo(LocalDate.of(1991, 2, 2));
        assertThat(user.getGender()).isEqualTo(Gender.OTHER);
        assertThat(user.getAvatarUrl()).isEqualTo("new-avatar");
        assertThat(user.getAddresses()).hasSize(1);
        assertThat(user.getAddresses().get(0).city()).isEqualTo("Hanoi");
        assertThat(user.getUpdatedAt()).isAfter(originalUpdatedAt);
    }

    @Test
    void update_replacesEntireAddressList() {
        User user = User.create("Alice", "alice@example.com", null, null, null, null,
            List.of(
                new Address(null, "A", null, null, "HCMC", "VN"),
                new Address(null, "B", null, null, "Hanoi", "VN")
            ));

        user.update("Alice", "alice@example.com", null, null, null, null, List.of());

        assertThat(user.getAddresses()).isEmpty();
    }

    @Test
    void update_throwsOnDeletedUser() {
        UserId id = UserId.generate();
        Instant now = Instant.now();
        User deleted = User.reconstitute(id, "Alice", "a@y.com", null, null, null, null,
            List.of(), now, now, now);

        assertThatThrownBy(() -> deleted.update("Bob", "b@y.com", null, null, null, null, null))
            .isInstanceOf(BusinessException.class)
            .satisfies(t -> {
                BusinessException ex = (BusinessException) t;
                assertThat(ex.getMessage()).isEqualTo("USER_ALREADY_DELETED");
            });
    }

    @Test
    void softDelete_setsDeletedAtAndIsDeletedReturnsTrue() {
        User user = User.create("Alice", "alice@example.com", null, null, null, null, null);
        assertThat(user.isDeleted()).isFalse();

        user.softDelete();

        assertThat(user.isDeleted()).isTrue();
        assertThat(user.getDeletedAt()).isNotNull();
    }

    @Test
    void softDelete_throwsWhenAlreadyDeleted() {
        UserId id = UserId.generate();
        Instant now = Instant.now();
        User deleted = User.reconstitute(id, "Alice", "a@y.com", null, null, null, null,
            List.of(), now, now, now);

        assertThatThrownBy(deleted::softDelete)
            .isInstanceOf(BusinessException.class)
            .satisfies(t -> {
                BusinessException ex = (BusinessException) t;
                assertThat(ex.getMessage()).isEqualTo("USER_ALREADY_DELETED");
            });
    }

    @Test
    void getAddresses_returnsUnmodifiableView() {
        User user = User.create("Alice", "alice@example.com", null, null, null, null,
            List.of(new Address(null, "A", null, null, "HCMC", "VN")));

        assertThatThrownBy(() -> user.getAddresses().clear())
            .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void create_succeedsWithAllNullOptionalFields() {
        assertThatCode(() -> User.create("Alice", "a@y.com", null, null, null, null, null))
            .doesNotThrowAnyException();
    }
}
