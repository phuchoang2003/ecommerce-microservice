package com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.adapter;

import com.hdp.product_service.application.port.out.ProductPersistencePort;
import com.hdp.product_service.domain.model.Product;
import com.hdp.product_service.domain.model.ProductVariant;
import com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.entity.ProductJpa;
import com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.mapper.ProductMapper;
import com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.repository.ProductRepositoryJpa;
import com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.repository.ProductVariantRepositoryJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements ProductPersistencePort {

    private final ProductRepositoryJpa productRepository;
    private final ProductVariantRepositoryJpa variantRepository;
    private final ProductMapper productMapper;

    @Override
    public Product save(Product product) {
        ProductJpa jpa = productMapper.toJpa(product);
        jpa = productRepository.save(jpa);
        return productMapper.toDomain(jpa, product.getVariants());
    }

    @Override
    public Optional<Product> findByIdAndNotDeleted(UUID id) {
        return productRepository.findByIdAndIsDeletedFalse(id)
                .map(jpa -> {
                    List<ProductVariant> variants = findVariantsByProductId(id);
                    return productMapper.toDomain(jpa, variants);
                });
    }

    @Override
    public Page<Product> findAll(Specification<Product> spec, Pageable pageable) {
        // Convert domain spec to JPA spec if needed
        return productRepository.findAll(pageable)
            .map(jpa -> {
                List<ProductVariant> variants = findVariantsByProductId(jpa.getId());
                return productMapper.toDomain(jpa, variants);
            });
    }

    @Override
    @Transactional
    public void softDelete(UUID id) {
        productRepository.findByIdAndIsDeletedFalse(id)
                .ifPresent(product -> {
                    product.setIsDeleted(true);
                    productRepository.save(product);
                });
    }

    @Override
    public List<ProductVariant> findVariantsByProductId(UUID productId) {
        return variantRepository.findByProductIdAndIsDeletedFalse(productId)
                .stream()
                .map(productMapper::toVariantDomain)
                .toList();
    }

    @Override
    @Transactional
    public void saveVariants(List<ProductVariant> variants) {
        // Implementation
    }

    @Override
    @Transactional
    public void deleteVariantsByProductId(UUID productId) {
        variantRepository.deleteByProductId(productId);
    }
}