package com.hdp.product_service.application.usecase;

import com.hdp.core.exception.NotFoundException;
import com.hdp.product_service.application.port.in.CreateProductUsecase;
import com.hdp.product_service.application.port.in.GetProductUsecase;
import com.hdp.product_service.application.port.out.ProductPersistencePort;
import com.hdp.product_service.domain.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetProductUsecaseImpl implements GetProductUsecase {

    private final ProductPersistencePort productPersistence;

    @Override
    @Transactional(readOnly = true)
    public CreateProductUsecase.Result execute(Command command) {
        Product product = productPersistence.findByIdAndNotDeleted(command.id())
            .orElseThrow(() -> new NotFoundException("Product", command.id()));

        log.info("Retrieved product: productId={}", command.id());
        return toResult(product);
    }

    private CreateProductUsecase.Result toResult(Product entity) {
        return new CreateProductUsecase.Result(
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