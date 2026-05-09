package com.hdp.product_service.infrastructure.adapter.inbound.web.controller;

import com.hdp.common.web.dto.response.ApiResponse;
import com.hdp.common.web.dto.response.PagedResponse;
import com.hdp.product_service.application.port.in.CreateProductUsecase;
import com.hdp.product_service.application.port.in.DeleteProductUsecase;
import com.hdp.product_service.application.port.in.GetProductUsecase;
import com.hdp.product_service.application.port.in.ListProductsUsecase;
import com.hdp.product_service.application.port.in.UpdateProductStatusUsecase;
import com.hdp.product_service.application.port.in.UpdateProductUsecase;
import com.hdp.product_service.infrastructure.adapter.inbound.web.dto.request.CreateProductRequest;
import com.hdp.product_service.infrastructure.adapter.inbound.web.dto.request.UpdateProductRequest;
import com.hdp.product_service.infrastructure.adapter.inbound.web.dto.request.UpdateProductStatusRequest;
import com.hdp.product_service.infrastructure.adapter.inbound.web.dto.response.ProductResponse;
import com.hdp.product_service.infrastructure.adapter.inbound.web.dto.response.ProductSummaryResponse;
import com.hdp.product_service.infrastructure.adapter.inbound.web.mapper.ProductWebMapper;
import com.hdp.core.request.PageQuery;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequestMapping("/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final CreateProductUsecase createProductUsecase;
    private final GetProductUsecase getProductUsecase;
    private final ListProductsUsecase listProductsUsecase;
    private final UpdateProductUsecase updateProductUsecase;
    private final UpdateProductStatusUsecase updateProductStatusUsecase;
    private final DeleteProductUsecase deleteProductUsecase;
    private final ProductWebMapper productWebMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody CreateProductRequest request) {
        CreateProductUsecase.Result result = createProductUsecase.execute(
            productWebMapper.toCreateCommand(request));
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(productWebMapper.toResponse(result), "Product created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(@PathVariable UUID id) {
        CreateProductUsecase.Result result = getProductUsecase.execute(new GetProductUsecase.Command(id));
        return ResponseEntity.ok(ApiResponse.success(productWebMapper.toResponse(result)));
    }

    @GetMapping
    public ResponseEntity<PagedResponse<ProductSummaryResponse>> listProducts(
            @RequestParam(required = false) UUID sellerId,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageQuery pageQuery = new PageQuery(page, size, List.of(), List.of());
        ListProductsUsecase.Command command = new ListProductsUsecase.Command(
            pageQuery, sellerId, categoryId,
            status != null ? com.hdp.product_service.domain.model.valueobject.ProductStatus.valueOf(status) : null,
            name);
        ListProductsUsecase.Result result = listProductsUsecase.execute(command);
        List<ProductSummaryResponse> responses = result.products().stream()
            .map(productWebMapper::toSummaryResponse).toList();
        return ResponseEntity.ok(PagedResponse.of(responses, page, size, result.totalElements()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProductRequest request) {
        CreateProductUsecase.Result result = updateProductUsecase.execute(
            productWebMapper.toUpdateCommand(id.toString(), request));
        return ResponseEntity.ok(ApiResponse.success(productWebMapper.toResponse(result), "Product updated successfully"));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProductStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProductStatusRequest request) {
        CreateProductUsecase.Result result = updateProductStatusUsecase.execute(
            productWebMapper.toUpdateStatusCommand(id.toString(), request));
        return ResponseEntity.ok(ApiResponse.success(productWebMapper.toResponse(result), "Product status updated"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable UUID id,
            @RequestParam UUID sellerId) {
        DeleteProductUsecase.Result result = deleteProductUsecase.execute(
            new DeleteProductUsecase.Command(id, sellerId));
        return ResponseEntity.ok(ApiResponse.success(null, "Product deleted successfully"));
    }
}