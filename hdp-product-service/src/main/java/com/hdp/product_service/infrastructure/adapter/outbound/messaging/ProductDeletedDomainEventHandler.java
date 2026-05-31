package com.hdp.product_service.infrastructure.adapter.outbound.messaging;

import com.hdp.common.messaging.publisher.OutboundEventPublisher;
import com.hdp.core.event.DomainEventHandler;
import com.hdp.messaging.event.product.ProductDeletedEventData;
import com.hdp.messaging.event.product.ProductDeletedIntegrationEvent;
import com.hdp.product_service.domain.event.ProductDeletedDomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductDeletedDomainEventHandler implements DomainEventHandler<ProductDeletedDomainEvent> {

    private final OutboundEventPublisher publisher;
    private static final String PRODUCT_DELETED_TOPIC = "product.deleted";

    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ProductDeletedDomainEvent event) {
        ProductDeletedEventData data = ProductDeletedEventData.newBuilder()
            .setProductId(event.getProductId().toString())
            .setSellerId(event.getSellerId().toString())
            .build();

        ProductDeletedIntegrationEvent avroEvent = ProductDeletedIntegrationEvent.newBuilder()
            .setEventId(UUID.randomUUID().toString())
            .setEventType("ProductDeleted")
            .setVersion(1)
            .setSource("product-service")
            .setCorrelationId(event.getEventId())
            .setOccurredAt(System.currentTimeMillis())
            .setData(data)
            .build();

//        publisher.send(avroEvent, PRODUCT_DELETED_TOPIC, event.getProductId().toString());
        log.info("Published ProductDeletedIntegrationEvent to Avro: productId={}", event.getProductId());
    }
}
