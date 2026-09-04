package com.hdp.customer_service.application.handler.createuser;

import com.hdp.core.exception.DuplicateKeyBusinessException;
import com.hdp.customer_service.application.port.in.createuser.CreateUserCommand;
import com.hdp.customer_service.application.port.out.UserPersistencePort;
import com.hdp.customer_service.domain.model.User;
import com.hdp.customer_service.domain.valueobject.Address;
import com.hdp.customer_service.domain.valueobject.Gender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateUserCommandHandlerImplTest {

    @Mock
    private UserPersistencePort userPersistence;

    @InjectMocks
    private CreateUserCommandHandlerImpl handler;

    @Test
    void handle_persistsAndReturnsResult() {
        CreateUserCommand cmd = new CreateUserCommand(
            "Alice", "alice@example.com", "+84123456789",
            LocalDate.of(1990, 1, 1), Gender.FEMALE, "https://cdn/a.png",
            List.of(new Address(null, "123 Le Loi", null, null, "HCMC", "VN"))
        );
        when(userPersistence.existsByEmail("alice@example.com")).thenReturn(false);
        when(userPersistence.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        var result = handler.handle(cmd);

        assertThat(result.fullName()).isEqualTo("Alice");
        assertThat(result.email()).isEqualTo("alice@example.com");
        assertThat(result.id()).isNotNull();
        assertThat(result.addresses()).hasSize(1);
        assertThat(result.createdAt()).isNotNull();
        assertThat(result.updatedAt()).isNotNull();
        verify(userPersistence, times(1)).save(any(User.class));
    }

    @Test
    void handle_acceptsEmptyAddressList() {
        CreateUserCommand cmd = new CreateUserCommand(
            "Bob", "bob@example.com", null, null, null, null, List.of()
        );
        when(userPersistence.existsByEmail("bob@example.com")).thenReturn(false);
        when(userPersistence.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        var result = handler.handle(cmd);

        assertThat(result.addresses()).isEmpty();
    }

    @Test
    void handle_throwsOnDuplicateEmail() {
        CreateUserCommand cmd = new CreateUserCommand(
            "Alice", "alice@example.com", null, null, null, null, null
        );
        when(userPersistence.existsByEmail("alice@example.com")).thenReturn(true);

        assertThatThrownBy(() -> handler.handle(cmd))
            .isInstanceOf(DuplicateKeyBusinessException.class);

        verify(userPersistence, never()).save(any(User.class));
    }

    @Test
    void handle_validatesProfileFields_throwsOnBlankFullName() {
        CreateUserCommand cmd = new CreateUserCommand(
            "  ", "alice@example.com", null, null, null, null, null
        );
        when(userPersistence.existsByEmail(anyString())).thenReturn(false);

        assertThatThrownBy(() -> handler.handle(cmd))
            .isInstanceOf(com.hdp.core.exception.BusinessException.class)
            .satisfies(t -> {
                com.hdp.core.exception.BusinessException ex = (com.hdp.core.exception.BusinessException) t;
                assertThat(ex.getMessage()).isEqualTo("USER_FIELD_REQUIRED");
                assertThat(ex.getMessageArgs()[0]).isEqualTo("fullName");
            });
    }
}
