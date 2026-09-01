package com.hdp.product_service.infrastructure.adapter.inbound.web.controller;

import com.hdp.common.web.dto.response.ApiResponse;
import com.hdp.common.web.dto.response.PagedResponse;
import com.hdp.product_service.application.port.in.createproduct.CreateProductCommand;
import com.hdp.product_service.application.port.in.createproduct.CreateProductCommandHandler;
import com.hdp.product_service.application.port.in.createproduct.CreateProductResult;
import com.hdp.product_service.application.port.in.deleteproduct.DeleteProductCommand;
import com.hdp.product_service.application.port.in.deleteproduct.DeleteProductCommandHandler;
import com.hdp.product_service.application.port.in.deleteproduct.DeleteProductResult;
import com.hdp.product_service.application.port.in.getproduct.GetProductQuery;
import com.hdp.product_service.application.port.in.getproduct.GetProductQueryHandler;
import com.hdp.product_service.application.port.in.getproduct.GetProductResult;
import com.hdp.product_service.application.port.in.listproducts.ListProductsQuery;
import com.hdp.product_service.application.port.in.listproducts.ListProductsQueryHandler;
import com.hdp.product_service.application.port.in.listproducts.ListProductsResult;
import com.hdp.product_service.application.port.in.listproducts.ProductSummary;
import com.hdp.product_service.application.port.in.updateproduct.UpdateProductCommand;
import com.hdp.product_service.application.port.in.updateproduct.UpdateProductCommandHandler;
import com.hdp.product_service.application.port.in.updateproduct.UpdateProductResult;
import com.hdp.product_service.application.port.in.updateproductstatus.UpdateProductStatusCommand;
import com.hdp.product_service.application.port.in.updateproductstatus.UpdateProductStatusCommandHandler;
import com.hdp.product_service.application.port.in.updateproductstatus.UpdateProductStatusResult;
import com.hdp.product_service.infrastructure.adapter.inbound.web.dto.request.CreateProductRequest;
import com.hdp.product_service.infrastructure.adapter.inbound.web.dto.request.UpdateProductRequest;
import com.hdp.product_service.infrastructure.adapter.inbound.web.dto.request.UpdateProductStatusRequest;
import com.hdp.product_service.infrastructure.adapter.inbound.web.dto.response.ProductResponse;
import com.hdp.product_service.infrastructure.adapter.inbound.web.dto.response.ProductSummaryResponse;
import com.hdp.product_service.infrastructure.adapter.inbound.web.mapper.ProductWebMapper;
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

    private final CreateProductCommandHandler createProductCommandHandler;
    private final GetProductQueryHandler getProductQueryHandler;
    private final ListProductsQueryHandler listProductsQueryHandler;
    private final UpdateProductCommandHandler updateProductCommandHandler;
    private final UpdateProductStatusCommandHandler updateProductStatusCommandHandler;
    private final DeleteProductCommandHandler deleteProductCommandHandler;
    private final ProductWebMapper productWebMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody CreateProductRequest request) {
        CreateProductResult result = createProductCommandHandler.handle(
            productWebMapper.toCreateCommand(request));
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(productWebMapper.toResponse(result), "Product created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(@PathVariable UUID id) {
        GetProductResult result = getProductQueryHandler.handle(new GetProductQuery(id));
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
        ListProductsQuery query = new ListProductsQuery(
            page, size, sellerId, categoryId,
            status != null ? com.hdp.product_service.domain.model.valueobject.ProductStatus.valueOf(status) : null,
            name);
        ListProductsResult result = listProductsQueryHandler.handle(query);
        List<ProductSummaryResponse> responses = result.products().stream()
            .map(productWebMapper::toSummaryResponse).toList();
        return ResponseEntity.ok(PagedResponse.of(responses, page, size, result.totalElements()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProductRequest request) {
        UpdateProductResult result = updateProductCommandHandler.handle(
            productWebMapper.toUpdateCommand(id.toString(), request));
        return ResponseEntity.ok(ApiResponse.success(productWebMapper.toResponse(result), "Product updated successfully"));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProductStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProductStatusRequest request) {
        UpdateProductStatusResult result = updateProductStatusCommandHandler.handle(
            productWebMapper.toUpdateStatusCommand(id.toString(), request));
        return ResponseEntity.ok(ApiResponse.success(productWebMapper.toResponse(result), "Product status updated"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable UUID id,
            @RequestParam UUID sellerId) {
        DeleteProductResult result = deleteProductCommandHandler.handle(
            new DeleteProductCommand(id, sellerId));
        return ResponseEntity.ok(ApiResponse.success(null, "Product deleted successfully"));
    }
}