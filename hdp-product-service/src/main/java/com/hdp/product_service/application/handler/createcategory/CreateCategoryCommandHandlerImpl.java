package com.hdp.product_service.application.handler.createcategory;

import com.hdp.product_service.application.port.in.createcategory.CreateCategoryCommand;
import com.hdp.product_service.application.port.in.createcategory.CreateCategoryCommandHandler;
import com.hdp.product_service.application.port.in.createcategory.CreateCategoryResult;
import com.hdp.product_service.application.port.out.CategoryPersistencePort;
import com.hdp.product_service.domain.model.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateCategoryCommandHandlerImpl implements CreateCategoryCommandHandler {

    private final CategoryPersistencePort categoryPersistence;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CreateCategoryResult handle(CreateCategoryCommand command) {
        String path = "";
        if (command.parentId() != null) {
            Category parent = categoryPersistence.findByIdAndNotDeleted(command.parentId())
                .orElseThrow(() -> new RuntimeException("Parent category not found: " + command.parentId()));
            path = parent.getPath() + "/" + slugify(parent.getName());
        } else {
            path = "/" + slugify(command.name());
        }

        Category category = Category.builder()
            .parentId(command.parentId())
            .name(command.name())
            .path(path)
            .build();

        Category saved = categoryPersistence.save(category);
        log.info("Category created: categoryId={}, name={}", saved.getId(), saved.getName());
        return toResult(saved);
    }

    private String slugify(String text) {
        return text.toLowerCase()
            .replaceAll("[^a-z0-9]+", "-")
            .replaceAll("^-|-$", "");
    }

    private CreateCategoryResult toResult(Category entity) {
        return new CreateCategoryResult(
            entity.getId(),
            entity.getParentId(),
            entity.getName(),
            entity.getPath(),
            entity.getCreatedAt()
        );
    }
}
