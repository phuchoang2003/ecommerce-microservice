package com.hdp.product_service.application.handler.listproducts;

import com.hdp.product_service.application.port.in.listproducts.ListProductsQuery;
import com.hdp.product_service.application.port.in.listproducts.ListProductsQueryHandler;
import com.hdp.product_service.application.port.in.listproducts.ListProductsResult;
import com.hdp.product_service.application.port.in.listproducts.ProductSummary;
import com.hdp.product_service.application.port.out.ProductPersistencePort;
import com.hdp.product_service.domain.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ListProductsQueryHandlerImpl implements ListProductsQueryHandler {

    private final ProductPersistencePort productPersistence;

    @Override
    @Transactional(readOnly = true)
    public ListProductsResult handle(ListProductsQuery query) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(query.page(), query.size(), sort);

        Page<Product> page = productPersistence.findAll(null, pageable);

        List<ProductSummary> products = page.getContent().stream()
            .map(this::toSummary)
            .toList();

        return new ListProductsResult(
            products,
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.hasNext(),
            page.hasPrevious()
        );
    }

    private ProductSummary toSummary(Product entity) {
        return new ProductSummary(
            entity.getId(),
            entity.getSellerId(),
            entity.getCategoryId(),
            entity.getName(),
            entity.getPrice(),
            entity.getImages(),
            entity.getRating(),
            entity.getSoldCount(),
            entity.getStatus()
        );
    }
}
