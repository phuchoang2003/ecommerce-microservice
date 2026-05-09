package com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.entity;

import com.hdp.common.persistence.entity.BaseEntityJpa;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class CategoryJpa extends BaseEntityJpa {

    @Column(name = "parent_id")
    private UUID parentId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "path", length = 500)
    private String path;
}