package com.hdp.product_service.application.usecase;

import com.hdp.product_service.application.port.in.CreateProductUsecase;
import com.hdp.product_service.application.port.in.ListProductsUsecase;
import com.hdp.product_service.application.port.out.ProductPersistencePort;
import com.hdp.product_service.domain.model.Product;
import com.hdp.core.request.PageQuery;
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
public class ListProductsUsecaseImpl implements ListProductsUsecase {

    private final ProductPersistencePort productPersistence;

    @Override
    @Transactional(readOnly = true)
    public Result execute(Command command) {
        PageQuery pq = command.pageQuery() != null ? command.pageQuery() : new PageQuery(0, 20, List.of(), List.of());

        Sort sort = Sort.by(Sort.Direction.fromString(
            pq.sorts().isEmpty() ? "DESC" : pq.sorts().get(0).direction().name()),
            pq.sorts().isEmpty() ? "createdAt" : pq.sorts().get(0).field());

        Pageable pageable = PageRequest.of(pq.page(), pq.size(), sort);

        Page<Product> page = productPersistence.findAll(null, pageable);

        List<CreateProductUsecase.Result> products = page.getContent().stream()
            .map(this::toResult)
            .toList();

        return new Result(
            products,
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.hasNext(),
            page.hasPrevious()
        );
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