package com.hdp.product_service.infrastructure.adapter.inbound.web.controller;

import com.hdp.common.web.dto.response.ApiResponse;
import com.hdp.product_service.application.port.in.createcategory.CreateCategoryCommand;
import com.hdp.product_service.application.port.in.createcategory.CreateCategoryCommandHandler;
import com.hdp.product_service.application.port.in.createcategory.CreateCategoryResult;
import com.hdp.product_service.application.port.in.deletecategory.DeleteCategoryCommand;
import com.hdp.product_service.application.port.in.deletecategory.DeleteCategoryCommandHandler;
import com.hdp.product_service.application.port.in.deletecategory.DeleteCategoryResult;
import com.hdp.product_service.application.port.in.getcategory.GetCategoryQuery;
import com.hdp.product_service.application.port.in.getcategory.GetCategoryQueryHandler;
import com.hdp.product_service.application.port.in.getcategory.GetCategoryResult;
import com.hdp.product_service.application.port.in.listcategories.ListCategoriesQuery;
import com.hdp.product_service.application.port.in.listcategories.ListCategoriesQueryHandler;
import com.hdp.product_service.application.port.in.listcategories.ListCategoriesResult;
import com.hdp.product_service.application.port.in.updatecategory.UpdateCategoryCommand;
import com.hdp.product_service.application.port.in.updatecategory.UpdateCategoryCommandHandler;
import com.hdp.product_service.application.port.in.updatecategory.UpdateCategoryResult;
import com.hdp.product_service.infrastructure.adapter.inbound.web.dto.request.CreateCategoryRequest;
import com.hdp.product_service.infrastructure.adapter.inbound.web.dto.request.UpdateCategoryRequest;
import com.hdp.product_service.infrastructure.adapter.inbound.web.dto.response.CategoryResponse;
import com.hdp.product_service.infrastructure.adapter.inbound.web.mapper.CategoryWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CreateCategoryCommandHandler createCategoryCommandHandler;
    private final UpdateCategoryCommandHandler updateCategoryCommandHandler;
    private final GetCategoryQueryHandler getCategoryQueryHandler;
    private final ListCategoriesQueryHandler listCategoriesQueryHandler;
    private final DeleteCategoryCommandHandler deleteCategoryCommandHandler;
    private final CategoryWebMapper categoryWebMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CreateCategoryRequest request) {
        CreateCategoryResult result = createCategoryCommandHandler.handle(
            categoryWebMapper.toCreateCommand(request));
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(categoryWebMapper.toResponse(result), "Category created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategory(@PathVariable UUID id) {
        GetCategoryResult result = getCategoryQueryHandler.handle(new GetCategoryQuery(id));
        return ResponseEntity.ok(ApiResponse.success(categoryWebMapper.toResponse(result)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> listCategories(
            @RequestParam(defaultValue = "false") boolean tree) {
        ListCategoriesResult result = listCategoriesQueryHandler.handle(
            new ListCategoriesQuery(tree));
        List<CategoryResponse> responses = result.categories().stream()
            .map(categoryWebMapper::toResponse).toList();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCategoryRequest request) {
        UpdateCategoryResult result = updateCategoryCommandHandler.handle(
            categoryWebMapper.toUpdateCommand(id.toString(), request));
        return ResponseEntity.ok(ApiResponse.success(categoryWebMapper.toResponse(result), "Category updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable UUID id) {
        DeleteCategoryResult result = deleteCategoryCommandHandler.handle(
            new DeleteCategoryCommand(id));
        return ResponseEntity.ok(ApiResponse.success(null, "Category deleted successfully"));
    }
}