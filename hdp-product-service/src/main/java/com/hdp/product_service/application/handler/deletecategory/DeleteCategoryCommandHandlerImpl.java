package com.hdp.product_service.application.handler.deletecategory;

import com.hdp.core.exception.BusinessException;
import com.hdp.core.exception.CoreErrorCode;
import com.hdp.core.exception.NotFoundException;
import com.hdp.product_service.application.port.in.deletecategory.DeleteCategoryCommand;
import com.hdp.product_service.application.port.in.deletecategory.DeleteCategoryCommandHandler;
import com.hdp.product_service.application.port.in.deletecategory.DeleteCategoryResult;
import com.hdp.product_service.application.port.out.CategoryPersistencePort;
import com.hdp.product_service.domain.model.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteCategoryCommandHandlerImpl implements DeleteCategoryCommandHandler {

    private final CategoryPersistencePort categoryPersistence;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeleteCategoryResult handle(DeleteCategoryCommand command) {
        Category category = categoryPersistence.findByIdAndNotDeleted(command.id())
            .orElseThrow(() -> new NotFoundException("Category", command.id()));

        if (!categoryPersistence.findByParentIdAndNotDeleted(command.id()).isEmpty()) {
            throw new BusinessException(CoreErrorCode.BUSINESS_ERROR,
                "Cannot delete category with children");
        }

        categoryPersistence.softDelete(command.id());
        log.info("Category deleted: categoryId={}", command.id());
        return new DeleteCategoryResult(command.id(), true);
    }
}
