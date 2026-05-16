package com.hdp.order_service.application.eventhandler;

import com.hdp.common.messaging.dispatcher.AvroEventHandler;
import com.hdp.messaging.event.product.ProductCreatedIntegrationEvent;
import com.hdp.order_service.application.port.out.ProductionSnapshotPersistencePort;
import com.hdp.order_service.domain.model.valueobject.ProductSnapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
        log.info("=== ProductCreatedIntegrationEventHandler.handle() called ===");
        log.info("record class: {}", record.getClass().getName());
        log.info("eventId: {}", record.getEventId());
        log.info("eventType: {}", record.getEventType());
        log.info("data.productId: {}", record.getData().getProductId());
        log.info("data.name: {}", record.getData().getName());

        var data = record.getData();
        UUID productId = UUID.fromString(data.getProductId());

        ProductSnapshot snapshot = ProductSnapshot.builder()
                .productId(productId)
                .variantId(productId)
                .productName(data.getName())
                .variantName(data.getName())
                .price(data.getPrice())
                .build();

        log.info("Saving snapshot for productId={}", productId);
        snapshotPersistencePort.save(snapshot);
        log.info("Product snapshot saved successfully: productId={}", productId);
    }
}
