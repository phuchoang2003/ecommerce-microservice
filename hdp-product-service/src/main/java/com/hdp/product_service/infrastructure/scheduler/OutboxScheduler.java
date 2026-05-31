package com.hdp.product_service.infrastructure.scheduler;

import com.hdp.product_service.application.port.out.OutboxEventPersistencePort;
import com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.entity.OutboxEventJpa;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxScheduler {
    private static final int MAX_RETRIES = 3;

    private final OutboxEventPersistencePort outboxEventPersistence;
    private final OutboxProcessor outboxProcessor;

    @Scheduled(fixedDelay = 5000)
    public void readOutboxEvents() {
        int batchSize = 100;
        int maxRetries = 3;
        log.debug("Reading outbox events: batchSize={}, maxRetries={}", batchSize, maxRetries);
        // Fetch pending and retryable events ordered by createdAt
        List<OutboxEventJpa> events = outboxEventPersistence.findBatchEvents(maxRetries, batchSize);

        if (events.isEmpty()) {
            log.trace("No pending outbox events found");
            return;
        }

        log.debug("Fetched {} pending outbox events", events.size());

        // Group events by messageKey to ensure sequential processing per aggregate
        Map<String, List<OutboxEventJpa>> eventsByKey = events.stream()
                .collect(Collectors.groupingBy(OutboxEventJpa::getMessageKey));

        // Process each key group
        for (Map.Entry<String, List<OutboxEventJpa>> entry : eventsByKey.entrySet()) {
            String key = entry.getKey();
            List<OutboxEventJpa> keyEvents = entry.getValue();

            log.trace("Processing key={}, eventCount={}", key, keyEvents.size());
            processKeyGroup(key, keyEvents);
        }
    }

    private void processKeyGroup(String key, List<OutboxEventJpa> events) {
        for (int i = 0; i < events.size(); i++) {
            EventProcessResult result = processEvent(events.get(i), key);
            if (result == EventProcessResult.FAILED) {
                markRemainingAsFailed(events, i);
                break;
            }
        }
    }

    private enum EventProcessResult { SUCCESS, SKIPPED, FAILED }

    private EventProcessResult processEvent(OutboxEventJpa event, String key) {
        if (event.getRetryCount() >= MAX_RETRIES) {
            log.info("Event id={} for key={} exceeded max retries ({}), marking as FAILED",
                    event.getId(), key, MAX_RETRIES);
            outboxEventPersistence.markAsFailed(event.getId());
            return EventProcessResult.SKIPPED;
        }
        if (outboxProcessor.processEvent(event)) {
            log.trace("Event id={} for key={} processed successfully", event.getId(), key);
            return EventProcessResult.SUCCESS;
        }
        return EventProcessResult.FAILED;
    }

    private void markRemainingAsFailed(List<OutboxEventJpa> events, int failedIndex) {
        List<OutboxEventJpa> remaining = events.subList(failedIndex + 1, events.size());
        if (!remaining.isEmpty()) {
            log.debug("Event id={} failed, marking {} subsequent events as FAILED",
                    events.get(failedIndex).getId(), remaining.size());
            markAllAsFailed(remaining);
        }
    }

    private void markAllAsFailed(List<OutboxEventJpa> events) {
        for (OutboxEventJpa event : events) {
            outboxEventPersistence.markAsFailed(event.getId());
        }
    }
}
