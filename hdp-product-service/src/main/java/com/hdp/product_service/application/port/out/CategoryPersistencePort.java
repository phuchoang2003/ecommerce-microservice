package com.hdp.product_service.application.port.out;

import com.hdp.product_service.domain.model.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryPersistencePort {

    Category save(Category category);

    Optional<Category> findByIdAndNotDeleted(UUID id);

    List<Category> findAllAndNotDeleted();

    List<Category> findByParentIdAndNotDeleted(UUID parentId);

    List<Category> findRootCategoriesAndNotDeleted();

    void softDelete(UUID id);
}