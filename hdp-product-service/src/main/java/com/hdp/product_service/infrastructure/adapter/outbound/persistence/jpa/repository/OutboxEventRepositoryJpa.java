package com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.repository;

import com.hdp.product_service.infrastructure.adapter.outbound.persistence.jpa.entity.OutboxEventJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface OutboxEventRepositoryJpa extends JpaRepository<OutboxEventJpa, UUID> {

    @Query("""
            SELECT o FROM OutboxEventJpa o
            WHERE o.status = 'PENDING'
               OR (o.status = 'FAILED' AND o.retryCount < :maxRetries)
            ORDER BY o.createdAt ASC
            LIMIT :batchSize
            """)
    List<OutboxEventJpa> findBatchEventsByStatus(int maxRetries, int batchSize);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE outbox_events SET status = :status, published_at = :publishedAt WHERE id = :id", nativeQuery = true)
    void markAsPublished(@Param("id") UUID id,
                         @Param("status") String status,
                         @Param("publishedAt") Instant publishedAt);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE outbox_events SET status = 'FAILED', retry_count = retry_count + 1 WHERE id = :id", nativeQuery = true)
    void markAsFailed(@Param("id") UUID id);
}