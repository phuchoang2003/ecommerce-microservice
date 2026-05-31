package com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.adapter;

import com.hdp.core.event.DomainEvent;
import com.hdp.product_service.application.port.out.OutboxEventPersistencePort;
import com.hdp.product_service.domain.model.valueobject.OutboxEventStatus;
import com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.entity.OutboxEventJpa;
import com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.mapper.OutboxEventJpaMapper;
import com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.repository.OutboxEventRepositoryJpa;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxEventPersistenceAdapter implements OutboxEventPersistencePort {
    private final OutboxEventRepositoryJpa outboxEventRepository;
    private final OutboxEventJpaMapper outboxEventJpaMapper;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void save(DomainEvent event, Object payload, String key) {
        try {
            OutboxEventJpa outboxEventJpa = outboxEventJpaMapper.toOutboxEventJpa(event, payload, key);
            outboxEventRepository.save(outboxEventJpa);
        }
        catch (IOException e) {
            log.error("Failed to serialize event payload: eventType={}, payload={}", event.getEventType(), payload, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<OutboxEventJpa> findBatchEvents(int maxRetries, int batchSize) {
        return outboxEventRepository.findBatchEventsByStatus(maxRetries, batchSize);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void markAsPublished(UUID id, Instant publishedAt) {
        outboxEventRepository.markAsPublished(id, String.valueOf(OutboxEventStatus.SENT), publishedAt);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void markAsFailed(UUID id) {
        outboxEventRepository.markAsFailed(id);
    }
}
