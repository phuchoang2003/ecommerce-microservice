package com.hdp.order_service.application.port.out;

import com.hdp.order_service.domain.model.valueobject.ProductSnapshot;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ProductionSnapshotPersistencePort {

    Map<UUID, ProductSnapshot> findByProductIdIn(List<UUID> productIds);

    void validateProductExists(UUID productId, UUID variantId);

    void save(ProductSnapshot snapshot);
}
