package com.hdp.customer_service.application.handler.deleteuser;

import com.hdp.core.exception.BusinessException;
import com.hdp.core.exception.NotFoundException;
import com.hdp.customer_service.application.port.in.deleteuser.DeleteUserCommand;
import com.hdp.customer_service.application.port.out.UserPersistencePort;
import com.hdp.customer_service.domain.model.User;
import com.hdp.customer_service.domain.valueobject.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteUserCommandHandlerImplTest {

    @Mock
    private UserPersistencePort userPersistence;

    @InjectMocks
    private DeleteUserCommandHandlerImpl handler;

    @Test
    void handle_softDeletesAndSaves() {
        UUID id = UUID.randomUUID();
        User existing = User.create("Alice", "alice@example.com", null, null, null, null, null);
        when(userPersistence.getById(UserId.of(id))).thenReturn(existing);
        when(userPersistence.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        Void result = handler.handle(new DeleteUserCommand(id));

        assertThat(result).isNull();
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userPersistence, times(1)).save(captor.capture());
        User saved = captor.getValue();
        assertThat(saved.isDeleted()).isTrue();
        assertThat(saved.getDeletedAt()).isNotNull();
    }

    @Test
    void handle_throwsNotFoundWhenUserMissing() {
        UUID id = UUID.randomUUID();
        when(userPersistence.getById(UserId.of(id))).thenReturn(null);

        assertThatThrownBy(() -> handler.handle(new DeleteUserCommand(id)))
            .isInstanceOf(NotFoundException.class);

        verify(userPersistence, never()).save(any(User.class));
    }

    @Test
    void handle_throwsWhenUserAlreadyDeleted() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        User deleted = User.reconstitute(
            UserId.of(id), "Alice", "a@y.com", null, null, null, null,
            java.util.List.of(), now, now, now
        );
        when(userPersistence.getById(UserId.of(id))).thenReturn(deleted);

        assertThatThrownBy(() -> handler.handle(new DeleteUserCommand(id)))
            .isInstanceOf(BusinessException.class)
            .satisfies(t -> {
                BusinessException ex = (BusinessException) t;
                assertThat(ex.getMessage()).isEqualTo("USER_ALREADY_DELETED");
            });

        verify(userPersistence, never()).save(any(User.class));
    }
}
