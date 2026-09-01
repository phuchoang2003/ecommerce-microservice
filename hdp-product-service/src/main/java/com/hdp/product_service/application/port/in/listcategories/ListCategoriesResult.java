package com.hdp.product_service.application.port.in.listcategories;

import java.util.List;

public record ListCategoriesResult(List<CategoryItem> categories) {
}
