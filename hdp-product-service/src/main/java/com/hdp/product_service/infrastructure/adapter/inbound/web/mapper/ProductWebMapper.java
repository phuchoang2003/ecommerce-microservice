package com.hdp.product_service.infrastructure.adapter.inbound.web.mapper;

import com.hdp.product_service.application.port.in.createproduct.CreateProductCommand;
import com.hdp.product_service.application.port.in.createproduct.CreateProductResult;
import com.hdp.product_service.application.port.in.getproduct.GetProductResult;
import com.hdp.product_service.application.port.in.listproducts.ProductSummary;
import com.hdp.product_service.application.port.in.updateproduct.UpdateProductCommand;
import com.hdp.product_service.application.port.in.updateproduct.UpdateProductResult;
import com.hdp.product_service.application.port.in.updateproductstatus.UpdateProductStatusCommand;
import com.hdp.product_service.application.port.in.updateproductstatus.UpdateProductStatusResult;
import com.hdp.product_service.domain.model.valueobject.ProductStatus;
import com.hdp.product_service.infrastructure.adapter.inbound.web.dto.request.CreateProductRequest;
import com.hdp.product_service.infrastructure.adapter.inbound.web.dto.request.UpdateProductRequest;
import com.hdp.product_service.infrastructure.adapter.inbound.web.dto.request.UpdateProductStatusRequest;
import com.hdp.product_service.infrastructure.adapter.inbound.web.dto.response.ProductResponse;
import com.hdp.product_service.infrastructure.adapter.inbound.web.dto.response.ProductSummaryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class ProductWebMapper {

    public CreateProductCommand toCreateCommand(CreateProductRequest request) {
        return new CreateProductCommand(
            UUID.fromString(request.sellerId()),
            UUID.fromString(request.categoryId()),
            request.name(),
            request.description(),
            request.price(),
            request.originalPrice(),
            request.images(),
            request.status()
        );
    }

    public UpdateProductCommand toUpdateCommand(String id, UpdateProductRequest request) {
        return new UpdateProductCommand(
            UUID.fromString(id),
            UUID.fromString(request.sellerId()),
            request.categoryId() != null ? UUID.fromString(request.categoryId()) : null,
            request.name(),
            request.description(),
            request.price(),
            request.originalPrice(),
            request.images(),
            request.status()
        );
    }

    public UpdateProductStatusCommand toUpdateStatusCommand(String id, UpdateProductStatusRequest request) {
        return new UpdateProductStatusCommand(
            UUID.fromString(id),
            UUID.fromString(request.sellerId()),
            request.newStatus()
        );
    }

    public ProductResponse toResponse(CreateProductResult result) {
        return new ProductResponse(
            result.id().toString(),
            result.sellerId().toString(),
            result.categoryId().toString(),
            result.name(),
            result.description(),
            result.price(),
            result.originalPrice(),
            result.images(),
            result.rating(),
            result.soldCount(),
            result.status().name(),
            result.createdAt(),
            result.updatedAt()
        );
    }

    public ProductResponse toResponse(GetProductResult result) {
        return new ProductResponse(
            result.id().toString(),
            result.sellerId().toString(),
            result.categoryId().toString(),
            result.name(),
            result.description(),
            result.price(),
            result.originalPrice(),
            result.images(),
            result.rating(),
            result.soldCount(),
            result.status().name(),
            result.createdAt(),
            result.updatedAt()
        );
    }

    public ProductResponse toResponse(UpdateProductResult result) {
        return new ProductResponse(
            result.id().toString(),
            result.sellerId().toString(),
            result.categoryId().toString(),
            result.name(),
            result.description(),
            result.price(),
            result.originalPrice(),
            result.images(),
            result.rating(),
            result.soldCount(),
            result.status().name(),
            result.createdAt(),
            result.updatedAt()
        );
    }

    public ProductResponse toResponse(UpdateProductStatusResult result) {
        return new ProductResponse(
            result.id().toString(),
            result.sellerId().toString(),
            result.categoryId().toString(),
            result.name(),
            result.description(),
            result.price(),
            result.originalPrice(),
            result.images(),
            result.rating(),
            result.soldCount(),
            result.status().name(),
            result.createdAt(),
            result.updatedAt()
        );
    }

    public ProductSummaryResponse toSummaryResponse(ProductSummary summary) {
        return new ProductSummaryResponse(
            summary.id().toString(),
            summary.sellerId().toString(),
            summary.categoryId().toString(),
            summary.name(),
            summary.price(),
            summary.images(),
            summary.rating(),
            summary.soldCount(),
            summary.status().name()
        );
    }
}
