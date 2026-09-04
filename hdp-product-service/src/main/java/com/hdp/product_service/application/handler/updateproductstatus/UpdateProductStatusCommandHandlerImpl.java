package com.hdp.product_service.application.handler.updateproductstatus;

import com.hdp.core.event.DomainEventPublisher;
import com.hdp.core.exception.BusinessException;
import com.hdp.core.exception.CoreErrorCode;
import com.hdp.core.exception.NotFoundException;
import com.hdp.product_service.application.port.in.updateproductstatus.UpdateProductStatusCommand;
import com.hdp.product_service.application.port.in.updateproductstatus.UpdateProductStatusCommandHandler;
import com.hdp.product_service.application.port.in.updateproductstatus.UpdateProductStatusResult;
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
public class UpdateProductStatusCommandHandlerImpl implements UpdateProductStatusCommandHandler {

    private final ProductPersistencePort productPersistence;
    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UpdateProductStatusResult handle(UpdateProductStatusCommand command) {
        Product product = productPersistence.findByIdAndNotDeleted(command.id())
            .orElseThrow(() -> new NotFoundException("Product", command.id()));

        if (!product.getSellerId().equals(command.sellerId())) {
            throw new BusinessException(CoreErrorCode.FORBIDDEN,
                "Seller not authorized to update this product");
        }

        product.updateStatus(command.newStatus());
        Product saved = productPersistence.save(product);
        publishProductUpdatedEvent(saved);

        log.info("Product status updated: productId={}, newStatus={}", command.id(), command.newStatus());
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

    private UpdateProductStatusResult toResult(Product entity) {
        return new UpdateProductStatusResult(
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
