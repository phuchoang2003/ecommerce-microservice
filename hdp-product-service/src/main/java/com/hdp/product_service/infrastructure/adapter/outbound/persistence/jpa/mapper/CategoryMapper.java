package com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.mapper;

import com.hdp.product_service.domain.model.Category;
import com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.entity.CategoryJpa;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toDomain(CategoryJpa jpa) {
        if (jpa == null) return null;
        return Category.builder()
                .id(jpa.getId())
                .parentId(jpa.getParentId())
                .name(jpa.getName())
                .path(jpa.getPath())
                .createdAt(jpa.getCreatedAt())
                .updatedAt(jpa.getUpdatedAt())
                .build();
    }

    public CategoryJpa toJpa(Category domain) {
        if (domain == null) return null;
        return CategoryJpa.builder()
                .id(domain.getId())
                .parentId(domain.getParentId())
                .name(domain.getName())
                .path(domain.getPath())
                .build();
    }

    public void updateJpaFromDomain(CategoryJpa jpa, Category domain) {
        if (domain.getName() != null) jpa.setName(domain.getName());
        if (domain.getParentId() != null) jpa.setParentId(domain.getParentId());
        if (domain.getPath() != null) jpa.setPath(domain.getPath());
    }
}