package com.hdp.product_service.application.handler.getcategory;

import com.hdp.core.exception.NotFoundException;
import com.hdp.product_service.application.port.in.getcategory.GetCategoryQuery;
import com.hdp.product_service.application.port.in.getcategory.GetCategoryQueryHandler;
import com.hdp.product_service.application.port.in.getcategory.GetCategoryResult;
import com.hdp.product_service.application.port.out.CategoryPersistencePort;
import com.hdp.product_service.domain.model.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetCategoryQueryHandlerImpl implements GetCategoryQueryHandler {

    private final CategoryPersistencePort categoryPersistence;

    @Override
    @Transactional(readOnly = true)
    public GetCategoryResult handle(GetCategoryQuery query) {
        Category category = categoryPersistence.findByIdAndNotDeleted(query.id())
            .orElseThrow(() -> new NotFoundException("Category", query.id()));

        log.info("Retrieved category: categoryId={}", query.id());
        return toResult(category);
    }

    private GetCategoryResult toResult(Category entity) {
        return new GetCategoryResult(
            entity.getId(),
            entity.getParentId(),
            entity.getName(),
            entity.getPath(),
            entity.getCreatedAt()
        );
    }
}
