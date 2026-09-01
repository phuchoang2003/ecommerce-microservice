package com.hdp.product_service.infrastructure.scheduler;

import com.hdp.common.messaging.publisher.OutboundEventPublisher;
import com.hdp.messaging.event.product.ProductCreatedIntegrationEvent;
import com.hdp.messaging.event.product.ProductDeletedIntegrationEvent;
import com.hdp.product_service.application.port.out.OutboxEventPersistencePort;
import com.hdp.product_service.constant.KafkaTopicConstants;
import com.hdp.product_service.domain.model.valueobject.OutboxEventType;
import com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.entity.OutboxEventJpa;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxProcessor {
    private final OutboundEventPublisher publisher;
    private final OutboxEventPersistencePort outboxEventPersistence;

    public boolean processEvent(OutboxEventJpa outboxEvent) {
        try {
            Object event = deserialize(outboxEvent);
            sendToKafka(event, outboxEvent);
            outboxEventPersistence.markAsPublished(outboxEvent.getId(), Instant.now());
            log.info("Outbox event processed successfully: id={}, type={}", outboxEvent.getId(), outboxEvent.getEventType());
            return true;
        } catch (Exception e) {
            log.error("Failed to process outbox event: id={}, type={}, retryCount={}",
                outboxEvent.getId(), outboxEvent.getEventType(), outboxEvent.getRetryCount(), e);
            outboxEventPersistence.markAsFailed(outboxEvent.getId());
            return false;
        }
    }

    private Object deserialize(OutboxEventJpa outboxEvent) throws Exception {
        ByteBuffer buffer = ByteBuffer.wrap(outboxEvent.getPayload());
        return switch (outboxEvent.getEventType()) {
            case PRODUCT_CREATED -> ProductCreatedIntegrationEvent.fromByteBuffer(buffer);
            case PRODUCT_DELETED -> ProductDeletedIntegrationEvent.fromByteBuffer(buffer);
        };
    }

    private void sendToKafka(Object event, OutboxEventJpa outboxEvent) {
        String topic = resolveTopic(outboxEvent.getEventType());
        publisher.send(event, topic, outboxEvent.getMessageKey());
    }

    public String resolveTopic(OutboxEventType eventType) {
        return switch (eventType) {
            case PRODUCT_CREATED, PRODUCT_DELETED -> KafkaTopicConstants.PRODUCT_EVENT_TOPIC;
        };
    }
}
