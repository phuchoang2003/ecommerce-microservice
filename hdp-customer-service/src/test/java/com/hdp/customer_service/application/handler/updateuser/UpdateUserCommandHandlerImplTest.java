package com.hdp.customer_service.application.handler.updateuser;

import com.hdp.core.exception.DuplicateKeyBusinessException;
import com.hdp.core.exception.NotFoundException;
import com.hdp.customer_service.application.port.in.updateuser.UpdateUserCommand;
import com.hdp.customer_service.application.port.out.UserPersistencePort;
import com.hdp.customer_service.domain.model.User;
import com.hdp.customer_service.domain.valueobject.Address;
import com.hdp.customer_service.domain.valueobject.Gender;
import com.hdp.customer_service.domain.valueobject.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateUserCommandHandlerImplTest {

    @Mock
    private UserPersistencePort userPersistence;

    @InjectMocks
    private UpdateUserCommandHandlerImpl handler;

    private User existingUser(String email) {
        return User.reconstitute(
            UserId.of(UUID.randomUUID()),
            "Alice", email, "+84123456789",
            LocalDate.of(1990, 1, 1), Gender.FEMALE, "old-avatar",
            List.of(new Address(null, "123 Le Loi", null, null, "HCMC", "VN")),
            null, Instant.now(), Instant.now()
        );
    }

    @Test
    void handle_updatesAndReturnsResult() {
        User existing = existingUser("alice@example.com");
        UUID id = existing.getId().value();
        when(userPersistence.getById(UserId.of(id))).thenReturn(existing);
        when(userPersistence.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UpdateUserCommand cmd = new UpdateUserCommand(
            id,
            "Alice Updated", "alice@example.com", "+84987654321",
            LocalDate.of(1991, 2, 2), Gender.OTHER, "new-avatar",
            List.of(new Address(null, "456 Tran Hung Dao", null, null, "Hanoi", "VN"))
        );

        var result = handler.handle(cmd);

        assertThat(result.id()).isEqualTo(id);
        assertThat(result.fullName()).isEqualTo("Alice Updated");
        assertThat(result.email()).isEqualTo("alice@example.com");
        assertThat(result.phone()).isEqualTo("+84987654321");
        assertThat(result.gender()).isEqualTo(Gender.OTHER);
        assertThat(result.avatarUrl()).isEqualTo("new-avatar");
        assertThat(result.addresses()).hasSize(1);
        assertThat(result.addresses().get(0).city()).isEqualTo("Hanoi");
        verify(userPersistence, times(1)).save(any(User.class));
    }

    @Test
    void handle_throwsNotFoundWhenUserMissing() {
        UUID id = UUID.randomUUID();
        when(userPersistence.getById(UserId.of(id))).thenReturn(null);

        UpdateUserCommand cmd = new UpdateUserCommand(
            id, "X", "x@y.com", null, null, null, null, null
        );

        assertThatThrownBy(() -> handler.handle(cmd))
            .isInstanceOf(NotFoundException.class);

        verify(userPersistence, never()).save(any(User.class));
    }

    @Test
    void handle_throwsDuplicateKeyWhenEmailChangedToExistingOne() {
        User existing = existingUser("alice@example.com");
        UUID id = existing.getId().value();
        when(userPersistence.getById(UserId.of(id))).thenReturn(existing);
        when(userPersistence.existsByEmail("bob@example.com")).thenReturn(true);

        UpdateUserCommand cmd = new UpdateUserCommand(
            id, "Alice", "bob@example.com", null, null, null, null, null
        );

        assertThatThrownBy(() -> handler.handle(cmd))
            .isInstanceOf(DuplicateKeyBusinessException.class);

        verify(userPersistence, never()).save(any(User.class));
    }

    @Test
    void handle_skipsDuplicateCheckWhenEmailUnchanged() {
        User existing = existingUser("alice@example.com");
        UUID id = existing.getId().value();
        when(userPersistence.getById(UserId.of(id))).thenReturn(existing);
        when(userPersistence.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UpdateUserCommand cmd = new UpdateUserCommand(
            id, "Alice Updated", "alice@example.com", null, null, null, null, null
        );

        handler.handle(cmd);

        verify(userPersistence, never()).existsByEmail(anyString());
    }

    @Test
    void handle_throwsOnDeletedUser() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        User deleted = User.reconstitute(
            UserId.of(id), "Alice", "alice@example.com", null, null, null, null,
            List.of(), now, now, now
        );
        when(userPersistence.getById(UserId.of(id))).thenReturn(deleted);

        UpdateUserCommand cmd = new UpdateUserCommand(
            id, "X", "x@y.com", null, null, null, null, null
        );

        assertThatThrownBy(() -> handler.handle(cmd))
            .isInstanceOf(com.hdp.core.exception.BusinessException.class)
            .satisfies(t -> {
                com.hdp.core.exception.BusinessException ex = (com.hdp.core.exception.BusinessException) t;
                assertThat(ex.getMessage()).isEqualTo("USER_ALREADY_DELETED");
            });

        verify(userPersistence, never()).save(any(User.class));
    }

    @Test
    void handle_replacesAddressesEntirely() {
        User existing = User.reconstitute(
            UserId.of(UUID.randomUUID()), "Alice", "a@y.com", null, null, null, null,
            List.of(
                new Address(null, "A", null, null, "HCMC", "VN"),
                new Address(null, "B", null, null, "Hanoi", "VN")
            ),
            null, Instant.now(), Instant.now()
        );
        UUID id = existing.getId().value();
        when(userPersistence.getById(UserId.of(id))).thenReturn(existing);
        when(userPersistence.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UpdateUserCommand cmd = new UpdateUserCommand(
            id, "Alice", "a@y.com", null, null, null, null, List.of()
        );

        var result = handler.handle(cmd);

        assertThat(result.addresses()).isEmpty();
    }
}
