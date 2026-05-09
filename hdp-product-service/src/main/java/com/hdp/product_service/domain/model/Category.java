package com.hdp.product_service.domain.model;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    private UUID id;
    private UUID parentId;
    private String name;
    private String path;
    private Instant createdAt;
    private Instant updatedAt;

    public void update(String name, UUID parentId, String path) {
        if (name != null) this.name = name;
        if (parentId != null) this.parentId = parentId;
        if (path != null) this.path = path;
        this.updatedAt = Instant.now();
    }
}