package com.hdp.product_service.application.usecase;

import com.hdp.core.event.DomainEventPublisher;
import com.hdp.core.exception.NotFoundException;
import com.hdp.product_service.application.port.in.CreateProductUsecase;
import com.hdp.product_service.application.port.out.CategoryPersistencePort;
import com.hdp.product_service.application.port.out.ProductPersistencePort;
import com.hdp.product_service.domain.event.ProductCreatedDomainEvent;
import com.hdp.product_service.domain.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateProductUsecaseImpl implements CreateProductUsecase {

    private final ProductPersistencePort productPersistence;
    private final CategoryPersistencePort categoryPersistence;
    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result execute(Command command) {
        // Validate category exists
        categoryPersistence.findByIdAndNotDeleted(command.categoryId())
            .orElseThrow(() -> new NotFoundException("Category", command.categoryId()));

        Product product = Product.builder()
            .sellerId(command.sellerId())
            .categoryId(command.categoryId())
            .name(command.name())
            .description(command.description())
            .price(command.price())
            .originalPrice(command.originalPrice())
            .images(new ArrayList<>(command.images()))
            .status(command.status())
            .variants(new ArrayList<>())
            .build();

        Product saved = productPersistence.save(product);
        publishProductCreatedEvent(saved);

        log.info("Product created: productId={}, name={}", saved.getId(), saved.getName());
        return toResult(saved);
    }

    private void publishProductCreatedEvent(Product product) {
        ProductCreatedDomainEvent event = ProductCreatedDomainEvent.builder()
            .productId(product.getId())
            .sellerId(product.getSellerId())
            .categoryId(product.getCategoryId())
            .name(product.getName())
            .price(product.getPrice())
            .images(product.getImages())
            .status(product.getStatus())
            .build();

        eventPublisher.publish(event);
        log.info("Published ProductCreatedDomainEvent: productId={}", product.getId());
    }

    private Result toResult(Product entity) {
        return new Result(
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