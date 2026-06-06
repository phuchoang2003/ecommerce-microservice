package com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.adapter;

import com.hdp.core.exception.NotFoundException;
import com.hdp.order_service.application.port.out.ProductionSnapshotPersistencePort;
import com.hdp.order_service.domain.model.valueobject.ProductSnapshot;
import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.entity.ProductSnapshotJpa;
import com.hdp.order_service.infrastructure.adapter.outbound.persistence.jpa.repository.ProductSnapshotRepositoryJpa;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductSnapshotPersistenceAdapter implements ProductionSnapshotPersistencePort {

    private final ProductSnapshotRepositoryJpa productSnapshotRepository;

    @Override
    @Transactional(readOnly = true)
    public Map<UUID, ProductSnapshot> findByProductIdIn(List<UUID> productIds) {
        List<ProductSnapshotJpa> entities = productSnapshotRepository.findByProductIdIn(productIds);
        return entities.stream()
            .collect(Collectors.toMap(ProductSnapshotJpa::getProductId, this::toProductSnapshot));
    }

    @Override
    @Transactional(readOnly = true)
    public void validateProductExists(UUID productId, UUID variantId) {
        if (!productSnapshotRepository.existsByProductId(productId)) {
            throw new NotFoundException("Product", productId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByProductIdAndVariantId(UUID productId, UUID variantId) {
        return productSnapshotRepository.findByProductIdAndVariantId(productId, variantId).isPresent();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ProductSnapshot snapshot) {
        ProductSnapshotJpa entity = ProductSnapshotJpa.builder()
                .productId(snapshot.productId())
                .variantId(snapshot.variantId())
                .productName(snapshot.productName())
                .variantName(snapshot.variantName())
                .price(snapshot.price())
                .build();

        try {
            productSnapshotRepository.save(entity);
        } catch (DuplicateKeyException e) {
            log.warn("Product snapshot already exists, skipping save: productId={}", snapshot.productId());
        }
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