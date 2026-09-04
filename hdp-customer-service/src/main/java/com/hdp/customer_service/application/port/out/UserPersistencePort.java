package com.hdp.customer_service.application.port.out;

import com.hdp.customer_service.domain.model.User;
import com.hdp.customer_service.domain.valueobject.UserId;

import java.util.Optional;
import java.util.UUID;

public interface UserPersistencePort {
    User save(User user);
    Optional<User> findById(UserId id);
    Optional<User> findByIdAndNotDeleted(UUID id);
    boolean existsByEmail(String email);
    User getById(UserId id);
}
