package com.hdp.product_service.application.handler.updateproduct;

import com.hdp.core.event.DomainEventPublisher;
import com.hdp.core.exception.BusinessException;
import com.hdp.core.exception.CoreErrorCode;
import com.hdp.core.exception.NotFoundException;
import com.hdp.product_service.application.port.in.updateproduct.UpdateProductCommand;
import com.hdp.product_service.application.port.in.updateproduct.UpdateProductCommandHandler;
import com.hdp.product_service.application.port.in.updateproduct.UpdateProductResult;
import com.hdp.product_service.application.port.out.CategoryPersistencePort;
import com.hdp.product_service.application.port.out.ProductPersistencePort;
import com.hdp.product_service.domain.event.ProductUpdatedDomainEvent;
import com.hdp.product_service.domain.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateProductCommandHandlerImpl implements UpdateProductCommandHandler {

    private final ProductPersistencePort productPersistence;
    private final CategoryPersistencePort categoryPersistence;
    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UpdateProductResult handle(UpdateProductCommand command) {
        Product product = productPersistence.findByIdAndNotDeleted(command.id())
            .orElseThrow(() -> new NotFoundException("Product", command.id()));

        if (!product.getSellerId().equals(command.sellerId())) {
            throw new BusinessException(CoreErrorCode.FORBIDDEN,
                "Seller not authorized to update this product");
        }

        if (command.categoryId() != null && !command.categoryId().equals(product.getCategoryId())) {
            categoryPersistence.findByIdAndNotDeleted(command.categoryId())
                .orElseThrow(() -> new NotFoundException("Category", command.categoryId()));
        }

        product.update(
            command.name(),
            command.description(),
            command.price(),
            command.originalPrice(),
            command.images(),
            command.status()
        );

        Product saved = productPersistence.save(product);
        publishProductUpdatedEvent(saved);

        log.info("Product updated: productId={}, name={}", saved.getId(), saved.getName());
        return toResult(saved);
    }

    private void publishProductUpdatedEvent(Product product) {
        ProductUpdatedDomainEvent event = ProductUpdatedDomainEvent.builder()
            .productId(product.getId())
            .sellerId(product.getSellerId())
            .name(product.getName())
            .price(product.getPrice())
            .images(product.getImages())
            .status(product.getStatus())
            .build();

        eventPublisher.publish(event);
        log.info("Published ProductUpdatedDomainEvent: productId={}", product.getId());
    }

    private UpdateProductResult toResult(Product entity) {
        return new UpdateProductResult(
            entity.getId(),
            entity.getSellerId(),
            entity.getCategoryId(),
            entity.getName(),
            entity.getDescription(),
            entity.getPrice(),
            entity.getOriginalPrice(),
            entity.getImages(),
            entity.getRating(),
            entity.getSoldCount(),
            entity.getStatus(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
