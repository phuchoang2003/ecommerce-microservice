package com.hdp.order_service.application.eventhandler;

import com.hdp.core.exception.DuplicateKeyBusinessException;
import com.hdp.messaging.event.product.ProductCreatedIntegrationEvent;
import com.hdp.order_service.application.port.out.ProductionSnapshotPersistencePort;
import com.hdp.order_service.domain.model.valueobject.ProductSnapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class ProductCreatedIntegrationEventHandler {

    private static final Logger log = LoggerFactory.getLogger(ProductCreatedIntegrationEventHandler.class);

    private final ProductionSnapshotPersistencePort snapshotPersistencePort;

    public ProductCreatedIntegrationEventHandler(ProductionSnapshotPersistencePort snapshotPersistencePort) {
        this.snapshotPersistencePort = snapshotPersistencePort;
    }

    @KafkaListener(topics = "product.events", groupId = "order-service")
    public void handle(ProductCreatedIntegrationEvent record, Acknowledgment acknowledgment) {
        try {
            if (record.getData() == null) {
                log.warn("ProductCreatedIntegrationEvent received with null data, skipping");
                acknowledgment.acknowledge();
                return;
            }

            var data = record.getData();
            if (data.getProductId() == null || data.getProductId().isBlank()) {
                log.warn("ProductCreatedIntegrationEvent received with null/empty productId, skipping");
                acknowledgment.acknowledge();
                return;
            }

            UUID productId;
            try {
                productId = UUID.fromString(data.getProductId());
            } catch (IllegalArgumentException e) {
                log.error("Invalid UUID format for productId: {}, skipping", data.getProductId(), e);
                acknowledgment.acknowledge();
                return;
            }

            String name = data.getName();
            BigDecimal price = data.getPrice();

            ProductSnapshot snapshot = ProductSnapshot.builder()
                    .productId(productId)
                    .variantId(productId)
                    .productName(name != null ? name : "Unknown")
                    .variantName(name != null ? name : "Unknown")
                    .price(price != null ? price : BigDecimal.ZERO)
                    .build();

            try {
                snapshotPersistencePort.save(snapshot);
                log.info("Product snapshot saved: productId={}, name={}, price={}",
                        productId, snapshot.productName(), snapshot.price());
            } catch (DuplicateKeyBusinessException e) {
                log.info("Duplicate product snapshot, treating as idempotent no-op: productId={}", productId);
            }

            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Failed to handle ProductCreatedIntegrationEvent: {}", e.getMessage(), e);
        }
    }
}