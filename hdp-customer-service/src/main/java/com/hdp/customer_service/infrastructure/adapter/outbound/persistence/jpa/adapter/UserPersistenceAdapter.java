package com.hdp.customer_service.infrastructure.adapter.outbound.persistence.jpa.adapter;

import com.hdp.customer_service.application.port.out.UserPersistencePort;
import com.hdp.customer_service.domain.model.User;
import com.hdp.customer_service.domain.valueobject.UserId;
import com.hdp.customer_service.infrastructure.adapter.outbound.persistence.jpa.entity.UserJpa;
import com.hdp.customer_service.infrastructure.adapter.outbound.persistence.jpa.repository.UserRepositoryJpa;
import com.hdp.customer_service.infrastructure.adapter.outbound.persistence.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPersistencePort {

    private final UserRepositoryJpa userRepository;
    private final UserMapper userMapper;

    @Override
    public User save(User user) {
        UserJpa jpa;
        if (user.getId() != null) {
            jpa = userRepository.findById(user.getId().value())
                .orElseGet(() -> UserJpa.builder().build());
        } else {
            jpa = UserJpa.builder().build();
        }

        userMapper.applyToJpa(user, jpa);
        UserJpa saved = userRepository.save(jpa);
        return userMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(UserId id) {
        if (id == null) {
            return Optional.empty();
        }
        return userRepository.findById(id.value())
            .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByIdAndNotDeleted(UUID id) {
        return userRepository.findByIdAndIsDeletedFalse(id)
            .map(userMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User getById(UserId id) {
        if (id == null) {
            return null;
        }
        return userRepository.findById(id.value())
            .map(userMapper::toDomain)
            .orElse(null);
    }
}
