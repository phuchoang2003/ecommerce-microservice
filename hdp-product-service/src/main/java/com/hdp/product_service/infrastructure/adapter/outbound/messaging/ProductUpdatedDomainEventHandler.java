package com.hdp.product_service.infrastructure.adapter.outbound.messaging;

import com.hdp.common.messaging.publisher.OutboundEventPublisher;
import com.hdp.core.event.DomainEventHandler;
import com.hdp.messaging.event.product.ProductUpdatedEventData;
import com.hdp.messaging.event.product.ProductUpdatedEvent;
import com.hdp.product_service.domain.event.ProductUpdatedDomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductUpdatedDomainEventHandler implements DomainEventHandler<ProductUpdatedDomainEvent> {

    private final OutboundEventPublisher publisher;
    private static final String PRODUCT_UPDATED_TOPIC = "product.updated";

    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ProductUpdatedDomainEvent event) {
        ProductUpdatedEventData data = ProductUpdatedEventData.newBuilder()
            .setProductId(event.getProductId().toString())
            .setSellerId(event.getSellerId().toString())
            .setName(event.getName())
            .setPrice(event.getPrice())
            .setImages(event.getImages())
            .setStatus(event.getStatus().name())
            .build();

        ProductUpdatedEvent avroEvent = ProductUpdatedEvent.newBuilder()
            .setEventId(UUID.randomUUID().toString())
            .setEventType("ProductUpdated")
            .setVersion(1)
            .setSource("product-service")
            .setCorrelationId(event.getEventId())
            .setOccurredAt(System.currentTimeMillis())
            .setData(data)
            .build();

//        publisher.send(avroEvent, PRODUCT_UPDATED_TOPIC, event.getProductId().toString());
        log.info("Published ProductUpdatedEvent to Avro: productId={}", event.getProductId());
    }
}
