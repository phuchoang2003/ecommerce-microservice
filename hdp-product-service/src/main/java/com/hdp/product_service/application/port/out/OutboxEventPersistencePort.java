package com.hdp.product_service.application.port.out;

import com.hdp.core.event.DomainEvent;
import com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.entity.OutboxEventJpa;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface OutboxEventPersistencePort {
    void save(DomainEvent event, Object payload, String key);
    List<OutboxEventJpa> findBatchEvents(int maxRetries, int batchSize);
    void markAsPublished(UUID id, Instant publishedAt);
    void markAsFailed(UUID id);
}
