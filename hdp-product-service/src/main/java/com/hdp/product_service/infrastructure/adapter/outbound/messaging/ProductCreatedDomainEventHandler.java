package com.hdp.product_service.infrastructure.adapter.outbound.messaging;

import com.hdp.core.constant.VersionConstant;
import com.hdp.core.event.DomainEventHandler;
import com.hdp.messaging.event.product.ProductCreatedEventData;
import com.hdp.messaging.event.product.ProductCreatedIntegrationEvent;
import com.hdp.product_service.application.port.out.OutboxEventPersistencePort;
import com.hdp.product_service.constant.ProductServiceConstants;
import com.hdp.product_service.domain.event.ProductCreatedDomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductCreatedDomainEventHandler implements DomainEventHandler<ProductCreatedDomainEvent> {

    private final OutboxEventPersistencePort outboxEventPersistence;

    @Override
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
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

        SpecificRecord avroEvent = ProductCreatedIntegrationEvent.newBuilder()
                .setEventId(event.getEventId())
                .setEventType(event.getEventType())
                .setVersion(VersionConstant.VERSION_NUMBER_1)
                .setSource(ProductServiceConstants.NAME)
                .setCorrelationId(event.getProductId().toString())
                .setOccurredAt(Instant.now().toEpochMilli())
                .setData(data)
                .build();


        outboxEventPersistence.save(event, avroEvent, event.getProductId().toString());
        log.info("Saved ProductCreatedEventData to Outbox: productId={}", event.getProductId());
    }
}
