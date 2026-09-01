package com.hdp.product_service.application.usecase;

import com.hdp.core.event.DomainEventPublisher;
import com.hdp.core.exception.BusinessException;
import com.hdp.core.exception.ErrorCode;
import com.hdp.core.exception.NotFoundException;
import com.hdp.product_service.application.port.in.DeleteProductUsecase;
import com.hdp.product_service.application.port.out.ProductPersistencePort;
import com.hdp.product_service.domain.event.ProductDeletedDomainEvent;
import com.hdp.product_service.domain.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteProductUsecaseImpl implements DeleteProductUsecase {

    private final ProductPersistencePort productPersistence;
    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result execute(Command command) {
        Product product = productPersistence.findByIdAndNotDeleted(command.id())
            .orElseThrow(() -> new NotFoundException("Product", command.id()));

        if (!product.getSellerId().equals(command.sellerId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN,
                "Seller not authorized to delete this product");
        }

        productPersistence.softDelete(command.id());
        publishProductDeletedEvent(product);

        log.info("Product deleted: productId={}", command.id());
        return new Result(command.id(), true);
    }

    private void publishProductDeletedEvent(Product product) {
        ProductDeletedDomainEvent event = ProductDeletedDomainEvent.builder()
            .productId(product.getId())
            .sellerId(product.getSellerId())
            .build();

        eventPublisher.publish(event);
        log.info("Published ProductDeletedDomainEvent: productId={}", product.getId());
    }
}