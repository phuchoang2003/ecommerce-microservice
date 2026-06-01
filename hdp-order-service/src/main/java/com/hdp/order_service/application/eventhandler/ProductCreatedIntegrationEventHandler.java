package com.hdp.order_service.application.eventhandler;

import com.hdp.common.messaging.dispatcher.AvroEventHandler;
import com.hdp.messaging.event.product.ProductCreatedIntegrationEvent;
import com.hdp.order_service.application.port.out.ProductionSnapshotPersistencePort;
import com.hdp.order_service.domain.model.valueobject.ProductSnapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class ProductCreatedIntegrationEventHandler implements AvroEventHandler<ProductCreatedIntegrationEvent> {

    private static final Logger log = LoggerFactory.getLogger(ProductCreatedIntegrationEventHandler.class);

    private final ProductionSnapshotPersistencePort snapshotPersistencePort;

    public ProductCreatedIntegrationEventHandler(ProductionSnapshotPersistencePort snapshotPersistencePort) {
        this.snapshotPersistencePort = snapshotPersistencePort;
    }

    @Override
    public void handle(ProductCreatedIntegrationEvent record) {
        if (record.getData() == null) {
            log.warn("ProductCreatedIntegrationEvent received with null data, skipping");
            return;
        }

        var data = record.getData();
        if (data.getProductId() == null || data.getProductId().isBlank()) {
            log.warn("ProductCreatedIntegrationEvent received with null/empty productId, skipping");
            return;
        }

        UUID productId;
        try {
            productId = UUID.fromString(data.getProductId());
        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format for productId: {}, skipping", data.getProductId(), e);
            return;
        }

        String name = data.getName();
        BigDecimal price = data.getPrice();

        if (snapshotPersistencePort.existsByProductIdAndVariantId(productId, productId)) {
            log.info("Product snapshot already exists, skipping save: productId={}", productId);
            return;
        }

        ProductSnapshot snapshot = ProductSnapshot.builder()
                .productId(productId)
                .variantId(productId)
                .productName(name != null ? name : "Unknown")
                .variantName(name != null ? name : "Unknown")
                .price(price != null ? price : BigDecimal.ZERO)
                .build();

        snapshotPersistencePort.save(snapshot);
        log.info("Product snapshot saved: productId={}, name={}, price={}",
                productId, snapshot.productName(), snapshot.price());
    }
}
