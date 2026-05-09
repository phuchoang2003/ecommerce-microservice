package com.hdp.product_service.application.port.out;

import com.hdp.product_service.domain.model.Product;
import com.hdp.product_service.domain.model.ProductVariant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductPersistencePort {

    Product save(Product product);

    Optional<Product> findByIdAndNotDeleted(UUID id);

    Page<Product> findAll(Specification<Product> spec, Pageable pageable);

    void softDelete(UUID id);

    List<ProductVariant> findVariantsByProductId(UUID productId);

    void saveVariants(List<ProductVariant> variants);

    void deleteVariantsByProductId(UUID productId);
}