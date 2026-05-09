package com.hdp.product_service.infrastructure.adapter.inbound.web.mapper;

import com.hdp.product_service.application.port.in.CreateCategoryUsecase;
import com.hdp.product_service.application.port.in.UpdateCategoryUsecase;
import com.hdp.product_service.infrastructure.adapter.inbound.web.dto.request.CreateCategoryRequest;
import com.hdp.product_service.infrastructure.adapter.inbound.web.dto.request.UpdateCategoryRequest;
import com.hdp.product_service.infrastructure.adapter.inbound.web.dto.response.CategoryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class CategoryWebMapper {

    public CreateCategoryUsecase.Command toCreateCommand(CreateCategoryRequest request) {
        return new CreateCategoryUsecase.Command(
            request.parentId() != null ? UUID.fromString(request.parentId()) : null,
            request.name()
        );
    }

    public UpdateCategoryUsecase.Command toUpdateCommand(String id, UpdateCategoryRequest request) {
        return new UpdateCategoryUsecase.Command(
            UUID.fromString(id),
            request.name(),
            request.parentId() != null ? UUID.fromString(request.parentId()) : null
        );
    }

    public CategoryResponse toResponse(CreateCategoryUsecase.Result result) {
        return new CategoryResponse(
            result.id().toString(),
            result.parentId() != null ? result.parentId().toString() : null,
            result.name(),
            result.path(),
            result.createdAt()
        );
    }
}