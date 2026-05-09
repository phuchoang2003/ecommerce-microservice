package com.hdp.product_service.infrastructure.adapter.outbound.messaging;

import com.hdp.common.messaging.publisher.OutboundEventPublisher;
import com.hdp.core.event.DomainEventHandler;
import com.hdp.messaging.event.product.ProductCreatedEventData;
import com.hdp.messaging.event.product.ProductCreatedIntegrationEvent;
import com.hdp.product_service.domain.event.ProductCreatedDomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductCreatedDomainEventHandler implements DomainEventHandler<ProductCreatedDomainEvent> {

    private final OutboundEventPublisher publisher;
    private static final String PRODUCT_CREATED_TOPIC = "product.created";

    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ProductCreatedDomainEvent event) {
        ProductCreatedEventData data = ProductCreatedEventData.newBuilder()
            .setProductId(event.getProductId().toString())
            .setSellerId(event.getSellerId().toString())
            .setCategoryId(event.getCategoryId() != null ? event.getCategoryId().toString() : null)
            .setName(event.getName())
            .setPrice(event.getPrice())
            .setImages(event.getImages())
            .setStatus(event.getStatus().name())
            .build();

        ProductCreatedIntegrationEvent avroEvent = ProductCreatedIntegrationEvent.newBuilder()
            .setEventId(UUID.randomUUID().toString())
            .setEventType("ProductCreated")
            .setVersion(1)
            .setSource("product-service")
            .setCorrelationId(event.getEventId())
            .setOccurredAt(System.currentTimeMillis())
            .setData(data)
            .build();

        publisher.send(avroEvent, PRODUCT_CREATED_TOPIC, event.getProductId().toString());
        log.info("Published ProductCreatedIntegrationEvent to Avro: productId={}", event.getProductId());
    }
}
