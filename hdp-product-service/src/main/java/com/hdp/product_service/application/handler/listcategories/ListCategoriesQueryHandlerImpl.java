package com.hdp.product_service.application.handler.listcategories;

import com.hdp.product_service.application.port.in.listcategories.CategoryItem;
import com.hdp.product_service.application.port.in.listcategories.ListCategoriesQuery;
import com.hdp.product_service.application.port.in.listcategories.ListCategoriesQueryHandler;
import com.hdp.product_service.application.port.in.listcategories.ListCategoriesResult;
import com.hdp.product_service.application.port.out.CategoryPersistencePort;
import com.hdp.product_service.domain.model.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ListCategoriesQueryHandlerImpl implements ListCategoriesQueryHandler {

    private final CategoryPersistencePort categoryPersistence;

    @Override
    @Transactional(readOnly = true)
    public ListCategoriesResult handle(ListCategoriesQuery query) {
        List<Category> categories;

        if (query.treeStructure()) {
            categories = categoryPersistence.findRootCategoriesAndNotDeleted();
        } else {
            categories = categoryPersistence.findAllAndNotDeleted();
        }

        List<CategoryItem> items = categories.stream()
            .map(this::toItem)
            .toList();

        log.info("Listed {} categories", items.size());
        return new ListCategoriesResult(items);
    }

    private CategoryItem toItem(Category entity) {
        return new CategoryItem(
            entity.getId(),
            entity.getParentId(),
            entity.getName(),
            entity.getPath(),
            entity.getCreatedAt()
        );
    }
}
