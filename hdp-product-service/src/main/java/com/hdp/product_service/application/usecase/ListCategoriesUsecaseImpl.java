package com.hdp.product_service.application.usecase;

import com.hdp.product_service.application.port.in.CreateCategoryUsecase;
import com.hdp.product_service.application.port.in.ListCategoriesUsecase;
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
public class ListCategoriesUsecaseImpl implements ListCategoriesUsecase {

    private final CategoryPersistencePort categoryPersistence;

    @Override
    @Transactional(readOnly = true)
    public Result execute(Command command) {
        List<Category> categories;

        if (command.treeStructure()) {
            // Return tree structure - root categories with children
            categories = categoryPersistence.findRootCategoriesAndNotDeleted();
        } else {
            categories = categoryPersistence.findAllAndNotDeleted();
        }

        List<CreateCategoryUsecase.Result> results = categories.stream()
            .map(this::toResult)
            .toList();

        log.info("Listed {} categories", results.size());
        return new Result(results);
    }

    private CreateCategoryUsecase.Result toResult(Category entity) {
        return new CreateCategoryUsecase.Result(
            entity.getId(),
            entity.getParentId(),
            entity.getName(),
            entity.getPath(),
            entity.getCreatedAt()
        );
    }
}