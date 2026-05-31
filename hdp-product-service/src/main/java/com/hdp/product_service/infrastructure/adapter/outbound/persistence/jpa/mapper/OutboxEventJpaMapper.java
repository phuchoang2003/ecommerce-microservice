package com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.mapper;

import com.hdp.core.event.DomainEvent;
import com.hdp.messaging.event.product.ProductCreatedIntegrationEvent;
import com.hdp.messaging.event.product.ProductDeletedIntegrationEvent;
import com.hdp.product_service.domain.model.valueobject.OutboxEventStatus;
import com.hdp.product_service.domain.model.valueobject.OutboxEventType;
import com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.entity.OutboxEventJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.ByteBuffer;

@Component
@RequiredArgsConstructor
public class OutboxEventJpaMapper {

    private final ObjectMapper objectMapper;

    public OutboxEventJpa toOutboxEventJpa(DomainEvent event, Object payload, String key) throws IOException {
        OutboxEventJpa outboxEventJpa = new OutboxEventJpa();
        outboxEventJpa.setEventType(OutboxEventType.fromString(event.getEventType()));
        outboxEventJpa.setStatus(OutboxEventStatus.PENDING);
        outboxEventJpa.setPayload(serialize(payload));
        outboxEventJpa.setMessageKey(key);
        return outboxEventJpa;
    }

    private byte[] serialize(Object payload) throws IOException {
        return switch (payload) {
            case ProductCreatedIntegrationEvent evt -> evt.toByteBuffer().array();
            case ProductDeletedIntegrationEvent evt -> evt.toByteBuffer().array();
            default -> objectMapper.writeValueAsString(payload).getBytes(java.nio.charset.StandardCharsets.UTF_8);
        };
    }
}