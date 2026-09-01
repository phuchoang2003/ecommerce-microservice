package com.hdp.product_service.infrastructure.adapter.inbound.web.mapper;

import com.hdp.product_service.application.port.in.createcategory.CreateCategoryCommand;
import com.hdp.product_service.application.port.in.createcategory.CreateCategoryResult;
import com.hdp.product_service.application.port.in.getcategory.GetCategoryResult;
import com.hdp.product_service.application.port.in.listcategories.CategoryItem;
import com.hdp.product_service.application.port.in.updatecategory.UpdateCategoryCommand;
import com.hdp.product_service.application.port.in.updatecategory.UpdateCategoryResult;
import com.hdp.product_service.infrastructure.adapter.inbound.web.dto.request.CreateCategoryRequest;
import com.hdp.product_service.infrastructure.adapter.inbound.web.dto.request.UpdateCategoryRequest;
import com.hdp.product_service.infrastructure.adapter.inbound.web.dto.response.CategoryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class CategoryWebMapper {

    public CreateCategoryCommand toCreateCommand(CreateCategoryRequest request) {
        return new CreateCategoryCommand(
            request.parentId() != null ? UUID.fromString(request.parentId()) : null,
            request.name()
        );
    }

    public UpdateCategoryCommand toUpdateCommand(String id, UpdateCategoryRequest request) {
        return new UpdateCategoryCommand(
            UUID.fromString(id),
            request.name(),
            request.parentId() != null ? UUID.fromString(request.parentId()) : null
        );
    }

    public CategoryResponse toResponse(CreateCategoryResult result) {
        return new CategoryResponse(
            result.id().toString(),
            result.parentId() != null ? result.parentId().toString() : null,
            result.name(),
            result.path(),
            result.createdAt()
        );
    }

    public CategoryResponse toResponse(UpdateCategoryResult result) {
        return new CategoryResponse(
            result.id().toString(),
            result.parentId() != null ? result.parentId().toString() : null,
            result.name(),
            result.path(),
            result.createdAt()
        );
    }

    public CategoryResponse toResponse(GetCategoryResult result) {
        return new CategoryResponse(
            result.id().toString(),
            result.parentId() != null ? result.parentId().toString() : null,
            result.name(),
            result.path(),
            result.createdAt()
        );
    }

    public CategoryResponse toResponse(CategoryItem item) {
        return new CategoryResponse(
            item.id().toString(),
            item.parentId() != null ? item.parentId().toString() : null,
            item.name(),
            item.path(),
            item.createdAt()
        );
    }
}
