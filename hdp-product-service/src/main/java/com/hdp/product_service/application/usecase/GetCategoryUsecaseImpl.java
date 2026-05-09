package com.hdp.product_service.application.usecase;

import com.hdp.core.exception.NotFoundException;
import com.hdp.product_service.application.port.in.CreateCategoryUsecase;
import com.hdp.product_service.application.port.in.GetCategoryUsecase;
import com.hdp.product_service.application.port.out.CategoryPersistencePort;
import com.hdp.product_service.domain.model.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetCategoryUsecaseImpl implements GetCategoryUsecase {

    private final CategoryPersistencePort categoryPersistence;

    @Override
    @Transactional(readOnly = true)
    public CreateCategoryUsecase.Result execute(Command command) {
        Category category = categoryPersistence.findByIdAndNotDeleted(command.id())
            .orElseThrow(() -> new NotFoundException("Category", command.id()));

        log.info("Retrieved category: categoryId={}", command.id());
        return toResult(category);
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