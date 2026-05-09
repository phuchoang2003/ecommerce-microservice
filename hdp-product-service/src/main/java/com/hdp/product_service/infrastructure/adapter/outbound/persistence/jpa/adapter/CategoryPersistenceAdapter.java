package com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.adapter;

import com.hdp.product_service.application.port.out.CategoryPersistencePort;
import com.hdp.product_service.domain.model.Category;
import com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.entity.CategoryJpa;
import com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.mapper.CategoryMapper;
import com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.repository.CategoryRepositoryJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryPersistenceAdapter implements CategoryPersistencePort {

    private final CategoryRepositoryJpa categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public Category save(Category category) {
        CategoryJpa jpa = categoryMapper.toJpa(category);
        jpa = categoryRepository.save(jpa);
        return categoryMapper.toDomain(jpa);
    }

    @Override
    public Optional<Category> findByIdAndNotDeleted(UUID id) {
        return categoryRepository.findByIdAndIsDeletedFalse(id)
                .map(categoryMapper::toDomain);
    }

    @Override
    public List<Category> findAllAndNotDeleted() {
        return categoryRepository.findAllByIsDeletedFalse()
                .stream()
                .map(categoryMapper::toDomain)
                .toList();
    }

    @Override
    public List<Category> findByParentIdAndNotDeleted(UUID parentId) {
        return categoryRepository.findByParentIdAndIsDeletedFalse(parentId)
                .stream()
                .map(categoryMapper::toDomain)
                .toList();
    }

    @Override
    public List<Category> findRootCategoriesAndNotDeleted() {
        return categoryRepository.findByParentIdIsNullAndIsDeletedFalse()
                .stream()
                .map(categoryMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public void softDelete(UUID id) {
        categoryRepository.findByIdAndIsDeletedFalse(id)
                .ifPresent(category -> {
                    category.setIsDeleted(true);
                    categoryRepository.save(category);
                });
    }
}