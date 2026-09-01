package com.hdp.product_service.application.port.in.updatecategory;

import java.util.UUID;

public record UpdateCategoryCommand(
    UUID id,
    String name,
    UUID parentId
) {
}
