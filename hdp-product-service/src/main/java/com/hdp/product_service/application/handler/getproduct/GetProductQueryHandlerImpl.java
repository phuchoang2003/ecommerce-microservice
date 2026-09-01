package com.hdp.product_service.application.handler.getproduct;

import com.hdp.core.exception.NotFoundException;
import com.hdp.product_service.application.port.in.getproduct.GetProductQuery;
import com.hdp.product_service.application.port.in.getproduct.GetProductQueryHandler;
import com.hdp.product_service.application.port.in.getproduct.GetProductResult;
import com.hdp.product_service.application.port.out.ProductPersistencePort;
import com.hdp.product_service.domain.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetProductQueryHandlerImpl implements GetProductQueryHandler {

    private final ProductPersistencePort productPersistence;

    @Override
    @Transactional(readOnly = true)
    public GetProductResult handle(GetProductQuery query) {
        Product product = productPersistence.findByIdAndNotDeleted(query.id())
            .orElseThrow(() -> new NotFoundException("Product", query.id()));

        log.info("Retrieved product: productId={}", query.id());
        return toResult(product);
    }

    private GetProductResult toResult(Product entity) {
        return new GetProductResult(
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
