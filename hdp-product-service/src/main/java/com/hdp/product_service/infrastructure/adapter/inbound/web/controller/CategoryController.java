package com.hdp.product_service.infrastructure.adapter.inbound.web.controller;

import com.hdp.common.web.dto.response.ApiResponse;
import com.hdp.product_service.application.port.in.CreateCategoryUsecase;
import com.hdp.product_service.application.port.in.DeleteCategoryUsecase;
import com.hdp.product_service.application.port.in.GetCategoryUsecase;
import com.hdp.product_service.application.port.in.ListCategoriesUsecase;
import com.hdp.product_service.application.port.in.UpdateCategoryUsecase;
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

    private final CreateCategoryUsecase createCategoryUsecase;
    private final UpdateCategoryUsecase updateCategoryUsecase;
    private final GetCategoryUsecase getCategoryUsecase;
    private final ListCategoriesUsecase listCategoriesUsecase;
    private final DeleteCategoryUsecase deleteCategoryUsecase;
    private final CategoryWebMapper categoryWebMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CreateCategoryRequest request) {
        CreateCategoryUsecase.Result result = createCategoryUsecase.execute(
            categoryWebMapper.toCreateCommand(request));
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(categoryWebMapper.toResponse(result), "Category created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategory(@PathVariable UUID id) {
        CreateCategoryUsecase.Result result = getCategoryUsecase.execute(new GetCategoryUsecase.Command(id));
        return ResponseEntity.ok(ApiResponse.success(categoryWebMapper.toResponse(result)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> listCategories(
            @RequestParam(defaultValue = "false") boolean tree) {
        ListCategoriesUsecase.Result result = listCategoriesUsecase.execute(
            new ListCategoriesUsecase.Command(tree));
        List<CategoryResponse> responses = result.categories().stream()
            .map(categoryWebMapper::toResponse).toList();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCategoryRequest request) {
        CreateCategoryUsecase.Result result = updateCategoryUsecase.execute(
            categoryWebMapper.toUpdateCommand(id.toString(), request));
        return ResponseEntity.ok(ApiResponse.success(categoryWebMapper.toResponse(result), "Category updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable UUID id) {
        DeleteCategoryUsecase.Result result = deleteCategoryUsecase.execute(
            new DeleteCategoryUsecase.Command(id));
        return ResponseEntity.ok(ApiResponse.success(null, "Category deleted successfully"));
    }
}