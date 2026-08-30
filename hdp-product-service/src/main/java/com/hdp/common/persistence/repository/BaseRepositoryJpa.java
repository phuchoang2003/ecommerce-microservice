package com.hdp.common.persistence.repository;

import com.hdp.common.persistence.entity.BaseEntityJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepositoryJpa<T extends BaseEntityJpa, ID extends Serializable> extends JpaRepository<T, ID> {

    @Query("SELECT e FROM #{#entityName} e WHERE e.id = :id AND e.isDeleted = false")
    Optional<T> findByIdAndNotDeleted(@Param("id") ID id);

    default void softDelete(T entity) {
        if (entity != null) {
            entity.setIsDeleted(true);
            save(entity);
        }
    }
}
