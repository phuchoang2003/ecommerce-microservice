package com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.adapter;

import com.hdp.core.exception.NotFoundException;
import com.hdp.order_service.application.port.out.ProductionSnapshotPersistencePort;
import com.hdp.order_service.domain.model.valueobject.ProductSnapshot;
import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.entity.ProductSnapshotJpa;
import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.repository.ProductSnapshotRepositoryJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductSnapshotPersistenceAdapter implements ProductionSnapshotPersistencePort {

    private final ProductSnapshotRepositoryJpa productSnapshotRepository;

    @Override
    public Map<UUID, ProductSnapshot> findByProductIdIn(List<UUID> productIds) {
        List<ProductSnapshotJpa> entities = productSnapshotRepository.findByProductIdIn(productIds);
        return entities.stream()
            .collect(Collectors.toMap(ProductSnapshotJpa::getProductId, this::toProductSnapshot));
    }

    @Override
    public void validateProductExists(UUID productId, UUID variantId) {
        if (!productSnapshotRepository.existsByProductId(productId)) {
            throw new NotFoundException("Product", productId);
        }
    }

    @Override
    @Transactional
    public void save(ProductSnapshot snapshot) {
        ProductSnapshotJpa entity = ProductSnapshotJpa.builder()
                .productId(snapshot.productId())
                .variantId(snapshot.variantId())
                .productName(snapshot.productName())
                .variantName(snapshot.variantName())
                .price(snapshot.price())
                .build();
        productSnapshotRepository.save(entity);
    }

    private ProductSnapshot toProductSnapshot(ProductSnapshotJpa entity) {
        return ProductSnapshot.builder()
            .productId(entity.getProductId())
            .variantId(entity.getVariantId())
            .productName(entity.getProductName())
            .variantName(entity.getVariantName())
            .price(entity.getPrice())
            .build();
    }
}