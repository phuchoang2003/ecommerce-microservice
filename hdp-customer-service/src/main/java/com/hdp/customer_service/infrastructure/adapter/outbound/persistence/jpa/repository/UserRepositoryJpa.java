package com.hdp.customer_service.infrastructure.adapter.outbound.persistence.jpa.repository;

import com.hdp.customer_service.infrastructure.adapter.outbound.persistence.jpa.entity.UserJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepositoryJpa extends JpaRepository<UserJpa, UUID> {

    Optional<UserJpa> findByIdAndIsDeletedFalse(UUID id);

    boolean existsByEmail(String email);
}
