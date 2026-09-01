package com.hdp.product_service.application.port.in.createcategory;

import java.util.UUID;

public record CreateCategoryCommand(
    UUID parentId,
    String name
) {
}
