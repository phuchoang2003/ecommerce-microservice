package com.hdp.customer_service.application.handler.deleteuser;

import com.hdp.core.exception.NotFoundException;
import com.hdp.customer_service.application.port.in.deleteuser.DeleteUserCommand;
import com.hdp.customer_service.application.port.out.UserPersistencePort;
import com.hdp.customer_service.domain.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
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
    void handle_marksUserAsDeleted() {
        UUID id = UUID.randomUUID();
        User existing = User.create("Alice", "alice@example.com", null, null, null, null, null);
        when(userPersistence.findByIdAndNotDeleted(id)).thenReturn(Optional.of(existing));

        Void result = handler.handle(new DeleteUserCommand(id));

        assertThat(result).isNull();
        verify(userPersistence, times(1)).softDelete(id);
    }

    @Test
    void handle_throwsNotFoundWhenUserMissing() {
        UUID id = UUID.randomUUID();
        when(userPersistence.findByIdAndNotDeleted(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> handler.handle(new DeleteUserCommand(id)))
            .isInstanceOf(NotFoundException.class);

        verify(userPersistence, never()).softDelete(any());
    }

    @Test
    void handle_throwsNotFoundWhenUserAlreadyDeleted() {
        UUID id = UUID.randomUUID();
        when(userPersistence.findByIdAndNotDeleted(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> handler.handle(new DeleteUserCommand(id)))
            .isInstanceOf(NotFoundException.class);

        verify(userPersistence, never()).softDelete(any());
    }
}