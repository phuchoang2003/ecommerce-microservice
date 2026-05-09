package com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.mapper;

import com.hdp.product_service.domain.model.Product;
import com.hdp.product_service.domain.model.ProductVariant;
import com.hdp.product_service.domain.model.valueobject.ProductStatus;
import com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.entity.ProductJpa;
import com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.entity.ProductVariantJpa;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public Product toDomain(ProductJpa jpa, List<ProductVariant> variants) {
        if (jpa == null) return null;
        return Product.builder()
                .id(jpa.getId())
                .sellerId(jpa.getSellerId())
                .categoryId(jpa.getCategoryId())
                .name(jpa.getName())
                .description(jpa.getDescription())
                .price(jpa.getPrice())
                .originalPrice(jpa.getOriginalPrice())
                .images(jpa.getImages())
                .rating(jpa.getRating())
                .soldCount(jpa.getSoldCount())
                .status(jpa.getStatus())
                .variants(variants)
                .createdAt(jpa.getCreatedAt())
                .updatedAt(jpa.getUpdatedAt())
                .build();
    }

    public ProductJpa toJpa(Product domain) {
        if (domain == null) return null;
        ProductJpa jpa = ProductJpa.builder()
                .id(domain.getId())
                .sellerId(domain.getSellerId())
                .categoryId(domain.getCategoryId())
                .name(domain.getName())
                .description(domain.getDescription())
                .price(domain.getPrice())
                .originalPrice(domain.getOriginalPrice())
                .images(domain.getImages())
                .rating(domain.getRating())
                .soldCount(domain.getSoldCount())
                .status(domain.getStatus())
                .build();
        return jpa;
    }

    public void updateJpaFromDomain(ProductJpa jpa, Product domain) {
        if (domain.getName() != null) jpa.setName(domain.getName());
        if (domain.getDescription() != null) jpa.setDescription(domain.getDescription());
        if (domain.getPrice() != null) jpa.setPrice(domain.getPrice());
        if (domain.getOriginalPrice() != null) jpa.setOriginalPrice(domain.getOriginalPrice());
        if (domain.getImages() != null) jpa.setImages(domain.getImages());
        if (domain.getStatus() != null) jpa.setStatus(domain.getStatus());
    }

    public ProductVariant toVariantDomain(ProductVariantJpa jpa) {
        if (jpa == null) return null;
        return ProductVariant.builder()
                .id(jpa.getId())
                .productId(jpa.getProduct().getId())
                .sku(jpa.getSku())
                .price(jpa.getPrice())
                .stock(jpa.getStock())
                .attributes(jpa.getAttributes())
                .createdAt(jpa.getCreatedAt())
                .updatedAt(jpa.getUpdatedAt())
                .build();
    }

    public ProductVariantJpa toVariantJpa(ProductVariant domain, ProductJpa productJpa) {
        if (domain == null) return null;
        return ProductVariantJpa.builder()
                .id(domain.getId())
                .product(productJpa)
                .sku(domain.getSku())
                .price(domain.getPrice())
                .stock(domain.getStock())
                .attributes(domain.getAttributes())
                .build();
    }

    public List<ProductVariant> toVariantDomainList(List<ProductVariantJpa> jpaList) {
        if (jpaList == null) return List.of();
        return jpaList.stream()
                .map(this::toVariantDomain)
                .collect(Collectors.toList());
    }
}