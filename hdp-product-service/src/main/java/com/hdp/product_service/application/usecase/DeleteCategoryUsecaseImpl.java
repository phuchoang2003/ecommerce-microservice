package com.hdp.product_service.application.usecase;

import com.hdp.core.exception.BusinessException;
import com.hdp.core.exception.ErrorCode;
import com.hdp.core.exception.NotFoundException;
import com.hdp.product_service.application.port.in.DeleteCategoryUsecase;
import com.hdp.product_service.application.port.out.CategoryPersistencePort;
import com.hdp.product_service.domain.model.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteCategoryUsecaseImpl implements DeleteCategoryUsecase {

    private final CategoryPersistencePort categoryPersistence;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result execute(Command command) {
        command.validate(command).throwIfInvalid();

        Category category = categoryPersistence.findByIdAndNotDeleted(command.id())
            .orElseThrow(() -> new NotFoundException("Category", command.id()));

        // Check if category has children
        if (!categoryPersistence.findByParentIdAndNotDeleted(command.id()).isEmpty()) {
            throw new BusinessException(ErrorCode.BUSINESS_ERROR,
                "Cannot delete category with children");
        }

        categoryPersistence.softDelete(command.id());
        log.info("Category deleted: categoryId={}", command.id());
        return new Result(command.id(), true);
    }
}