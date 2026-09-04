package com.hdp.customer_service.application.handler.getuser;

import com.hdp.core.exception.NotFoundException;
import com.hdp.customer_service.application.port.in.getuser.GetUserByIdQuery;
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
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserByIdQueryHandlerImplTest {

    @Mock
    private UserPersistencePort userPersistence;

    @InjectMocks
    private GetUserByIdQueryHandlerImpl handler;

    @Test
    void handle_returnsResultWithAddresses() {
        UUID id = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();
        User user = User.reconstitute(
            UserId.of(id), "Alice", "alice@example.com", "+84123456789",
            LocalDate.of(1990, 1, 1), Gender.FEMALE, "https://cdn/a.png",
            List.of(new Address(addressId, "123 Le Loi", null, null, "HCMC", "VN")),
            null, Instant.now(), Instant.now()
        );
        when(userPersistence.findByIdAndNotDeleted(id)).thenReturn(Optional.of(user));

        var result = handler.handle(new GetUserByIdQuery(id));

        assertThat(result.id()).isEqualTo(id);
        assertThat(result.fullName()).isEqualTo("Alice");
        assertThat(result.addresses()).hasSize(1);
        assertThat(result.addresses().get(0).id()).isEqualTo(addressId);
    }

    @Test
    void handle_throwsNotFoundWhenUserMissing() {
        UUID id = UUID.randomUUID();
        when(userPersistence.findByIdAndNotDeleted(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> handler.handle(new GetUserByIdQuery(id)))
            .isInstanceOf(NotFoundException.class);
    }
}
