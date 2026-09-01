package com.hdp.product_service.infrastructure.adapter.inbound.web.mapper;

import com.hdp.product_service.application.port.in.CreateProductUsecase;
import com.hdp.product_service.application.port.in.UpdateProductStatusUsecase;
import com.hdp.product_service.application.port.in.UpdateProductUsecase;
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

    public CreateProductUsecase.Command toCreateCommand(CreateProductRequest request) {
        return new CreateProductUsecase.Command(
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

    public UpdateProductUsecase.Command toUpdateCommand(String id, UpdateProductRequest request) {
        return new UpdateProductUsecase.Command(
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

    public UpdateProductStatusUsecase.Command toUpdateStatusCommand(String id, UpdateProductStatusRequest request) {
        return new UpdateProductStatusUsecase.Command(
            UUID.fromString(id),
            UUID.fromString(request.sellerId()),
            request.newStatus()
        );
    }

    public ProductResponse toResponse(CreateProductUsecase.Result result) {
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

    public ProductSummaryResponse toSummaryResponse(CreateProductUsecase.Result result) {
        return new ProductSummaryResponse(
            result.id().toString(),
            result.sellerId().toString(),
            result.categoryId().toString(),
            result.name(),
            result.price(),
            result.images(),
            result.rating(),
            result.soldCount(),
            result.status().name()
        );
    }
}