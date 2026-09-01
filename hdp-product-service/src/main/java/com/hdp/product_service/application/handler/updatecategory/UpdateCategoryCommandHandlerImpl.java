package com.hdp.product_service.application.handler.updatecategory;

import com.hdp.core.exception.NotFoundException;
import com.hdp.product_service.application.port.in.updatecategory.UpdateCategoryCommand;
import com.hdp.product_service.application.port.in.updatecategory.UpdateCategoryCommandHandler;
import com.hdp.product_service.application.port.in.updatecategory.UpdateCategoryResult;
import com.hdp.product_service.application.port.out.CategoryPersistencePort;
import com.hdp.product_service.domain.model.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateCategoryCommandHandlerImpl implements UpdateCategoryCommandHandler {

    private final CategoryPersistencePort categoryPersistence;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UpdateCategoryResult handle(UpdateCategoryCommand command) {
        Category category = categoryPersistence.findByIdAndNotDeleted(command.id())
            .orElseThrow(() -> new NotFoundException("Category", command.id()));

        if (command.name() != null) {
            String path;
            if (category.getParentId() != null) {
                Category parent = categoryPersistence.findByIdAndNotDeleted(category.getParentId())
                    .orElse(null);
                path = parent != null ? parent.getPath() + "/" + slugify(command.name()) : "/" + slugify(command.name());
            } else {
                path = "/" + slugify(command.name());
            }
            category.update(command.name(), command.parentId(), path);
        } else if (command.parentId() != null) {
            Category newParent = categoryPersistence.findByIdAndNotDeleted(command.parentId())
                .orElseThrow(() -> new NotFoundException("Parent Category", command.parentId()));
            category.update(null, command.parentId(), newParent.getPath() + "/" + slugify(category.getName()));
        }

        Category saved = categoryPersistence.save(category);
        log.info("Category updated: categoryId={}, name={}", saved.getId(), saved.getName());
        return toResult(saved);
    }

    private String slugify(String text) {
        return text.toLowerCase()
            .replaceAll("[^a-z0-9]+", "-")
            .replaceAll("^-|-$", "");
    }

    private UpdateCategoryResult toResult(Category entity) {
        return new UpdateCategoryResult(
            entity.getId(),
            entity.getParentId(),
            entity.getName(),
            entity.getPath(),
            entity.getCreatedAt()
        );
    }
}
